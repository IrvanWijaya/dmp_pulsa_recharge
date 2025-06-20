package dans_multi_pro.recharge_worker.dao;


import dans_multi_pro.recharge_worker.model.entity.RechargeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeRequestRepository extends JpaRepository<RechargeRequest, Long> {
}
