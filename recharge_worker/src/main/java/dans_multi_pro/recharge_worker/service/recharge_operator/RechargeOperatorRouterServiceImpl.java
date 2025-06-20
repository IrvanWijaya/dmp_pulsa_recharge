package dans_multi_pro.recharge_worker.service.recharge_operator;

import dans_multi_pro.recharge_worker.model.dto.RechargeQueueRequestDto;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class RechargeOperatorRouterServiceImpl implements RechargeOperatorRouterService{

    @Setter
    ApplicationContext applicationContext;

    @Override
    public void routeExecute(RechargeQueueRequestDto rechargeQueueRequestDto) {
        RechargeOperatorService operatorService = getOperatorService(rechargeQueueRequestDto);
        operatorService.executeRecharge(rechargeQueueRequestDto);
    }

    private RechargeOperatorService getOperatorService(RechargeQueueRequestDto rechargeQueueRequestDto) {
        return applicationContext.getBeansOfType(RechargeOperatorService.class)
                .values()
                .stream()
                .filter(service -> service.getOperatorType().equals(rechargeQueueRequestDto.getOperatorType()))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("No valid operator found.")
                );
    }
}
