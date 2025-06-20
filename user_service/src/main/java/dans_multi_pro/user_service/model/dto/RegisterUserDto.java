package dans_multi_pro.user_service.model.dto;

import dans_multi_pro.user_service.model.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserDto {
    private String username;
    private String password;
    private String name;

    public User getUserEntity() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);

        return user;
    }
}
