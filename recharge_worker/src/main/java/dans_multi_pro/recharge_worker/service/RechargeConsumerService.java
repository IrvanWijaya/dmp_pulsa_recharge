package dans_multi_pro.recharge_worker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import dans_multi_pro.recharge_worker.constant.RabbitMQConstants;
import dans_multi_pro.recharge_worker.model.dto.RechargeQueueRequestDto;
import dans_multi_pro.recharge_worker.service.recharge_operator.RechargeOperatorRouterService;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class RechargeConsumerService {

    private final RechargeOperatorRouterService rechargeOperatorRouterService;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private ZoneId zoneId;

    public RechargeConsumerService(
            RechargeOperatorRouterService rechargeOperatorRouterService,
            ObjectMapper objectMapper,
            RabbitTemplate rabbitTemplate,
            @Value("${timezone.zone-id}") String zoneId
    ) {
        this.rechargeOperatorRouterService = rechargeOperatorRouterService;
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.zoneId = ZoneId.of(zoneId);
    }

    @SneakyThrows
    @RabbitListener(queues = RabbitMQConstants.QUEUE_RUN_RECHARGE_REQUEST_THIRD_PARTY_RECHARGE_SERVICE, ackMode = "MANUAL")
    public void receiveMessage(@Payload String message,
                               Channel channel,
                               @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            RechargeQueueRequestDto runPayrollRequestDto = objectMapper.readValue(message, RechargeQueueRequestDto.class);
            rechargeOperatorRouterService.routeExecute(runPayrollRequestDto);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            channel.basicNack(tag, false, false); // send to DLQ
        }
    }
}