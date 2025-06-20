package dans_multi_pro.recharge_service.model.dto;

import dans_multi_pro.recharge_service.constant.OperatorTypeEnum;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RechargeRequestDto {
    private OperatorTypeEnum operatorType;
    private int amount;
}
