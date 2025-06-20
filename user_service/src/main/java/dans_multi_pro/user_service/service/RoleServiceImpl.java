package dans_multi_pro.user_service.service;

import dans_multi_pro.user_service.constant.UserRoleEnum;
import dans_multi_pro.user_service.dao.RoleRepository;
import dans_multi_pro.user_service.model.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findById(UserRoleEnum id) {
        Role role = roleRepository.findById(id);
        return role;
    }
}