package dans_multi_pro.recharge_service.service;

import dans_multi_pro.recharge_service.model.dto.RechargeRequestDto;
import dans_multi_pro.recharge_service.model.entity.RechargeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RechargeService {
    RechargeRequest recharge(RechargeRequestDto requestDto, long userId);
    RechargeRequest getStatus(long rechargeId, long userId);
    Page<RechargeRequest> getHistories(long userId, Pageable pageable);
}
