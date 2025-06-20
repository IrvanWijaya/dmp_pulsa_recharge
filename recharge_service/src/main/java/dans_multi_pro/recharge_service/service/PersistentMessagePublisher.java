package dans_multi_pro.recharge_service.service;

import dans_multi_pro.recharge_service.model.dto.RechargeRequestDto;
import dans_multi_pro.recharge_service.model.entity.RechargeRequest;

public interface PersistentMessagePublisher {
    void publish(String exchange, String routingKey, String messageId, String payload);
}
