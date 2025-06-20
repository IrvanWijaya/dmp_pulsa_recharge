package dans_multi_pro.recharge_service.service;

import dans_multi_pro.recharge_service.model.dto.ThirdPartyWebhookDto;

public interface RechargeWebhookService {
    boolean validateThirdPartyKey(String key);
    void updateData(ThirdPartyWebhookDto data);
}
