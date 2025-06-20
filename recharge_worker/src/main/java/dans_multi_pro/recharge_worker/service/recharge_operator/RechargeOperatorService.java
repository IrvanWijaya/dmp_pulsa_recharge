package dans_multi_pro.recharge_worker.service.recharge_operator;

import dans_multi_pro.recharge_worker.constant.OperatorTypeEnum;
import dans_multi_pro.recharge_worker.model.dto.RechargeQueueRequestDto;

public interface RechargeOperatorService {
    OperatorTypeEnum getOperatorType();
    void executeRecharge(RechargeQueueRequestDto rechargeQueueRequestDto);
}
