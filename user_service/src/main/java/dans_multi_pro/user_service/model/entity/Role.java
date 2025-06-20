package dans_multi_pro.user_service.model.entity;

import dans_multi_pro.user_service.constant.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {
    @Column
    @Id
    @Enumerated(EnumType.STRING)
    private UserRoleEnum id;

    @Column
    private String name;
}
