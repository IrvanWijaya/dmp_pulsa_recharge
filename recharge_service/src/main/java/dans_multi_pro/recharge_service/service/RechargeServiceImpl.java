package dans_multi_pro.recharge_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dans_multi_pro.recharge_service.constant.RabbitMQConstants;
import dans_multi_pro.recharge_service.constant.RechargeStatusEnum;
import dans_multi_pro.recharge_service.dao.RechargeRequestRepository;
import dans_multi_pro.recharge_service.model.dto.RechargeQueueRequestDto;
import dans_multi_pro.recharge_service.model.dto.RechargeRequestDto;
import dans_multi_pro.recharge_service.model.entity.RechargeRequest;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class RechargeServiceImpl implements RechargeService{

    private RechargeRequestRepository rechargeRequestRepository;
    private PersistentMessagePublisher persistentMessagePublisher;
    private ObjectMapper objectMapper;
    private ZoneId zoneId;

    public RechargeServiceImpl(
            RechargeRequestRepository rechargeRequestRepository,
            PersistentMessagePublisher persistentMessagePublisher,
            ObjectMapper objectMapper,
            @Value("${timezone.zone-id}") String zoneId
    ) {
        this.rechargeRequestRepository = rechargeRequestRepository;
        this.persistentMessagePublisher = persistentMessagePublisher;
        this.objectMapper = objectMapper;
        this.zoneId = ZoneId.of(zoneId);
    }

    @Override
    @Transactional
    @SneakyThrows
    public RechargeRequest recharge(RechargeRequestDto requestDto, long userId) {
        RechargeRequest rechargeRequest = RechargeRequest.builder()
                .userId(userId)
                .operator(requestDto.getOperatorType())
                .amount(requestDto.getAmount())
                .requestAt(ZonedDateTime.now(zoneId))
                .status(RechargeStatusEnum.IN_PROGRESS)
                .build();
        rechargeRequestRepository.save(rechargeRequest);
        rechargeRequestRepository.flush();

        queueRechargeRequest(rechargeRequest);

        return rechargeRequest;
    }

    private void queueRechargeRequest(RechargeRequest rechargeRequest) throws JsonProcessingException {
        RechargeQueueRequestDto runPayrollRequestDto = new RechargeQueueRequestDto(
                rechargeRequest.getId(),
                rechargeRequest.getOperator(),
                rechargeRequest.getAmount()
        );

        String message = objectMapper.writeValueAsString(runPayrollRequestDto);

        String messageId = UUID.randomUUID().toString();

        persistentMessagePublisher.publish(
                RabbitMQConstants.EXCHANGE_RUN_RECHARGE_DIRECT_EVENTS,
                RabbitMQConstants.ROUTING_KEY_RUN_RECHARGE_RECHARGE_EXECUTE,
                messageId,
                message
        );
    }

    @Override
    public RechargeRequest getStatus(long rechargeId, long userId) {

        RechargeRequest rechargeRequest = rechargeRequestRepository.findById(rechargeId).orElseThrow(
                () -> new RuntimeException("recharge not found")
        );

        if(rechargeRequest.getUserId() != userId){
            throw new RuntimeException("recharge not found");
        }

        return rechargeRequest;
    }

    @Override
    public Page<RechargeRequest> getHistories(long userId, Pageable pageable) {
        return rechargeRequestRepository.findByUserId(userId, pageable);
    }
}
