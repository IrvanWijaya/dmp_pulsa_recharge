package dans_multi_pro.recharge_service.dao;

import dans_multi_pro.recharge_service.model.entity.RechargeRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RechargeRequestRepository extends JpaRepository<RechargeRequest, Long> {

    Page<RechargeRequest> findByUserId(long userId, Pageable pageable);
    Optional<RechargeRequest> findByThirdPartyId(String thirdPartyId);

}
