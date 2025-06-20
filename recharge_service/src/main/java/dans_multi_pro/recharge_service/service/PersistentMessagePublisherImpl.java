package dans_multi_pro.recharge_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dans_multi_pro.recharge_service.model.dto.RetryMessageDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class PersistentMessagePublisherImpl implements PersistentMessagePublisher{

    private static final int DEFAULT_RETRY_TTL_SECONDS = 300;
    private static final int MAX_RETRIES = 3;

    private final RabbitTemplate rabbitTemplate;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    public PersistentMessagePublisherImpl(
            RabbitTemplate rabbitTemplate,
            RedisService redisService,
            ObjectMapper objectMapper
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.redisService = redisService;
        this.objectMapper = objectMapper;

        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String correlationId = correlationData != null ? correlationData.getId() : null;

            if (ack) {
                redisService.delete(correlationId);
            } else {
                redisService.getObject(correlationId, RetryMessageDto.class).ifPresent(dto -> {
                    if (dto.getRetryCount() < MAX_RETRIES) {
                        retry(correlationId, dto);
                    } else {
                        redisService.delete(correlationId);
                        //Should go to DLQ but for this demo we will let it bugged
                    }
                });
            }
        });
    }

    @Override
    @SneakyThrows
    public void publish(String exchange, String routingKey, String messageId, String payload) {
        String redisKey = "reliable-msg:" + messageId;
        String cacheValue = objectMapper.writeValueAsString(RetryMessageDto.builder()
                .message(payload)
                .retryCount(1)
                .exchangeName(exchange)
                .routingKey(routingKey)
                .build());
        redisService.put(redisKey, cacheValue, DEFAULT_RETRY_TTL_SECONDS);
        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                payload,
                msg -> {
                    msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return msg;
                },
                new CorrelationData(redisKey)
        );
    }

    @SneakyThrows
    private void retry(String correlationId, RetryMessageDto dto) {
        dto.setRetryCount(dto.getRetryCount() + 1);
        String cacheValue = objectMapper.writeValueAsString(dto);

        String newCorrelationId = correlationId+ "-retry-" + dto.getRetryCount();
        redisService.put(newCorrelationId, cacheValue, DEFAULT_RETRY_TTL_SECONDS);

        rabbitTemplate.convertAndSend(
                dto.getExchangeName(),
                dto.getRoutingKey(),
                dto.getMessage(),
                msg -> {
                    msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return msg;
                },
                new CorrelationData(newCorrelationId)
        );

    }
}
