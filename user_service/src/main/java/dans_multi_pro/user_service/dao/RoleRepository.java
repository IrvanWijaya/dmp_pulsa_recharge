package dans_multi_pro.user_service.dao;

import dans_multi_pro.user_service.constant.UserRoleEnum;
import dans_multi_pro.user_service.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findById(UserRoleEnum id);
}