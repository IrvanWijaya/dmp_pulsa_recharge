package dans_multi_pro.recharge_worker.service.recharge_operator;

import dans_multi_pro.recharge_worker.model.dto.RechargeQueueRequestDto;
import org.springframework.context.ApplicationContextAware;

public interface RechargeOperatorRouterService extends ApplicationContextAware {
    void routeExecute(RechargeQueueRequestDto rechargeQueueRequestDto);
}
