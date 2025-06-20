package dans_multi_pro.recharge_service.model.dto;


import dans_multi_pro.recharge_service.constant.OperatorTypeEnum;
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
