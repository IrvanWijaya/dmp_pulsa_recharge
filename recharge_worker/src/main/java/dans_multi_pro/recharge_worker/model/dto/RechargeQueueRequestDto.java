package dans_multi_pro.recharge_worker.model.dto;

import dans_multi_pro.recharge_worker.constant.OperatorTypeEnum;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RechargeQueueRequestDto {
    private long requestId;
    private OperatorTypeEnum operatorType;
    private int amount;
}
