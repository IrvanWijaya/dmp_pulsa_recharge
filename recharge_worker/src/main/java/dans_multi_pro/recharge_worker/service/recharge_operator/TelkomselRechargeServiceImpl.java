package dans_multi_pro.recharge_worker.service.recharge_operator;

import dans_multi_pro.recharge_worker.constant.OperatorTypeEnum;
import dans_multi_pro.recharge_worker.dao.RechargeRequestRepository;
import dans_multi_pro.recharge_worker.model.dto.RechargeQueueRequestDto;
import dans_multi_pro.recharge_worker.model.entity.RechargeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TelkomselRechargeServiceImpl implements RechargeOperatorService {

    @Autowired
    private RechargeRequestRepository rechargeRequestRepository;

    @Override
    public OperatorTypeEnum getOperatorType() {
        return OperatorTypeEnum.TELKOMSEL;
    }

    @Override
    @Transactional
    public void executeRecharge(RechargeQueueRequestDto rechargeQueueRequestDto) {
        //this should do the api call to the third party, for this demo lets just do the update third party id

        RechargeRequest rechargeRequest = rechargeRequestRepository.findById(rechargeQueueRequestDto.getRequestId())
                .orElseThrow(
                        () -> new RuntimeException("recharge request not found")
                );

        String id = OperatorTypeEnum.TELKOMSEL.name() + "-" + rechargeQueueRequestDto.getRequestId();

        rechargeRequest.setThirdPartyId(id);
        rechargeRequestRepository.save(rechargeRequest);
    }
}
