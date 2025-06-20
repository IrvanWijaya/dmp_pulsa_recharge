package dans_multi_pro.recharge_service.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RetryMessageDto {
    private String message;
    private int retryCount;
    private String exchangeName;
    private String routingKey;
}