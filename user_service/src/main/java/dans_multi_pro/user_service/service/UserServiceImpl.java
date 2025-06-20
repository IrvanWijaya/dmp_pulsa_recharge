package dans_multi_pro.user_service.service;


import dans_multi_pro.user_service.constant.UserRoleEnum;
import dans_multi_pro.user_service.dao.UserRepository;
import dans_multi_pro.user_service.model.dto.RegisterUserDto;
import dans_multi_pro.user_service.model.entity.Role;
import dans_multi_pro.user_service.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getId()));
        });
        return authorities;
    }

    @Override
    @Transactional
    public User save(RegisterUserDto user) {
        User nUser = user.getUserEntity();
        nUser.setPassword(bcryptEncoder.encode(user.getPassword()));

        Role role = roleService.findById(UserRoleEnum.USER);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        nUser.setRoles(roleSet);
        return userRepository.save(nUser);
    }

    @Override
    @Transactional
    public User saveAdmin(RegisterUserDto user) {
        User nUser = user.getUserEntity();
        nUser.setPassword(bcryptEncoder.encode(user.getPassword()));

        Role role = roleService.findById(UserRoleEnum.ADMIN);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        nUser.setRoles(roleSet);
        return userRepository.save(nUser);
    }
}