package dans_multi_pro.recharge_service.service;

import dans_multi_pro.recharge_service.dao.RechargeRequestRepository;
import dans_multi_pro.recharge_service.model.dto.ThirdPartyWebhookDto;
import dans_multi_pro.recharge_service.model.entity.RechargeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class RechargeWebhookServiceImpl implements RechargeWebhookService {

    @Value("dmp.third.party.key")
    private String thirdPartyKey;

    @Autowired
    private RechargeRequestRepository rechargeRequestRepository;

    @Value("${timezone.zone-id}")
    private String zoneId;

    @Override
    public boolean validateThirdPartyKey(String key) {
        return thirdPartyKey.equals(key);
    }

    @Override
    public void updateData(ThirdPartyWebhookDto data) {
        RechargeRequest rechargeRequest = rechargeRequestRepository.findByThirdPartyId(data.getId()).orElseThrow(
                () -> new RuntimeException("Third party id not found")
        );

        rechargeRequest.setStatus(data.getStatus());
        rechargeRequest.setEndAt(ZonedDateTime.now(ZoneId.of(zoneId)));
        rechargeRequestRepository.save(rechargeRequest);
    }
}
