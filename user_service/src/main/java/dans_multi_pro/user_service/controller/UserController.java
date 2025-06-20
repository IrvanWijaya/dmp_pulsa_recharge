package dans_multi_pro.user_service.controller;

import dans_multi_pro.user_service.common.ApiResponse;
import dans_multi_pro.user_service.common.aop.annotation.RequireRole;
import dans_multi_pro.user_service.constant.UserRoleEnum;
import dans_multi_pro.user_service.model.dto.AuthTokenDto;
import dans_multi_pro.user_service.model.dto.LoginUserDto;
import dans_multi_pro.user_service.model.dto.RegisterUserDto;
import dans_multi_pro.user_service.model.entity.User;
import dans_multi_pro.user_service.service.TokenService;
import dans_multi_pro.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<ApiResponse<AuthTokenDto>> generateToken(
            @RequestBody LoginUserDto loginUserDto,
            HttpServletResponse response
    ) throws AuthenticationException {
        return ResponseEntity.ok(ApiResponse.success(tokenService.getAuthJwt(loginUserDto, response)));
    }

    @PostMapping(value = "/refresh-token")
    public ResponseEntity<ApiResponse<AuthTokenDto>> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        return ResponseEntity.ok(ApiResponse.success(tokenService.refreshToken(request, response)));
    }

    @PostMapping(value="/register")
    public ApiResponse<User> createUser(@RequestBody RegisterUserDto user){
        return ApiResponse.success(userService.save(user));
    }

    @RequireRole(UserRoleEnum.ADMIN)
    @PostMapping(value="/register/admin")
    public ApiResponse<User> createAdminUser(@RequestBody RegisterUserDto user){
        return ApiResponse.success(userService.saveAdmin(user));
    }
}