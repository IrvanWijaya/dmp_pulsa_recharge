package dans_multi_pro.user_service.service;


import dans_multi_pro.user_service.model.dto.RegisterUserDto;
import dans_multi_pro.user_service.model.entity.User;

public interface UserService {
    User save(RegisterUserDto user);
    User saveAdmin(RegisterUserDto user);
}
