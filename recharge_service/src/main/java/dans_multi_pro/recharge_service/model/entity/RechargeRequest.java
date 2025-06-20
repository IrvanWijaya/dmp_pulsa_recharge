package dans_multi_pro.recharge_service.model.entity;

import dans_multi_pro.recharge_service.constant.OperatorTypeEnum;
import dans_multi_pro.recharge_service.constant.RechargeStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recharge_requests")
public class RechargeRequest {
    @Column
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column
    private long userId;

    @Column
    @Enumerated(EnumType.STRING)
    private OperatorTypeEnum operator;

    @Column
    private String thirdPartyId;

    @Column
    private int amount;

    @Column
    private ZonedDateTime requestAt;

    @Column
    @Enumerated(EnumType.STRING)
    private RechargeStatusEnum status;

    @Column
    private ZonedDateTime endAt;
}