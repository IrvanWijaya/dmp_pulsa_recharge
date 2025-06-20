package dans_multi_pro.user_service.service;


import dans_multi_pro.user_service.model.dto.AuthTokenDto;
import dans_multi_pro.user_service.model.dto.LoginUserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    AuthTokenDto getAuthJwt(LoginUserDto loginUserDto, HttpServletResponse response);
    AuthTokenDto refreshToken(HttpServletRequest request,
                              HttpServletResponse response);
}
