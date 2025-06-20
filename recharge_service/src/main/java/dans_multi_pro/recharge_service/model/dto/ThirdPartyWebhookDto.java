package dans_multi_pro.recharge_service.model.dto;

import dans_multi_pro.recharge_service.constant.OperatorTypeEnum;
import dans_multi_pro.recharge_service.constant.RechargeStatusEnum;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyWebhookDto {
    private String id;
    private RechargeStatusEnum status;
}
