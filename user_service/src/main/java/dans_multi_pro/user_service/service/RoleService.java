package dans_multi_pro.user_service.service;


import dans_multi_pro.user_service.constant.UserRoleEnum;
import dans_multi_pro.user_service.model.entity.Role;

public interface RoleService {
    Role findById(UserRoleEnum name);
}
