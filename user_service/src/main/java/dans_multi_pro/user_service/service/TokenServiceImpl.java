package dans_multi_pro.user_service.service;

import dans_multi_pro.user_service.config.TokenProvider;
import dans_multi_pro.user_service.dao.UserRepository;
import dans_multi_pro.user_service.model.dto.AuthTokenDto;
import dans_multi_pro.user_service.model.dto.LoginUserDto;
import dans_multi_pro.user_service.model.entity.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static dans_multi_pro.user_service.constant.RedisConstant.REDIS_USER_SESSION_PREFIX;
import static dans_multi_pro.user_service.constant.UserSessionKey.REFRESH_TOKEN;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    @Resource(name = "userService")
    private UserDetailsService userDetailsService;

    @Override
    public AuthTokenDto getAuthJwt(LoginUserDto loginUserDto, HttpServletResponse response) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getUsername(),
                        loginUserDto.getPassword()
                )
        );

        User userEntity = userRepository.findByUsername(loginUserDto.getUsername()).orElseThrow(
                () -> new RuntimeException("User Not Found")
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateAccessToken(authentication, userEntity.getId());

        String refreshId = UUID.randomUUID().toString();
        final String refreshToken = jwtTokenUtil.generateRefreshToken(authentication, refreshId);

        addRefreshTokenToCookie(response, refreshToken);
        storeUserSessionToRedis(userEntity, refreshToken);

        return new AuthTokenDto(token);
    }

    private static void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/users/refresh-token")
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    private void storeUserSessionToRedis(User userEntity, String refreshToken) {
        String redisKey = REDIS_USER_SESSION_PREFIX + userEntity.getId();
        Map<String, String> sessionData = new HashMap<>();
        sessionData.put(REFRESH_TOKEN, refreshToken);
        redisService.hmset(redisKey, sessionData, 60 * 60 * 24 * 7);
    }

    @Override
    public AuthTokenDto refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RuntimeException("No cookies found");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> REFRESH_TOKEN.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Refresh token missing"));

        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtTokenUtil.validateToken(refreshToken, userDetails)) {
            throw new RuntimeException("Invalid refresh token");
        }

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User Not Found")
        );

        String redisKey = REDIS_USER_SESSION_PREFIX + user.getId();
        String storedRefreshToken = redisService.hget(redisKey, REFRESH_TOKEN);

        if (!refreshToken.equals(storedRefreshToken)) {
            throw new RuntimeException("Refresh token mismatch");
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        final String newAccessToken = jwtTokenUtil.generateAccessToken(authentication, user.getId());

        String newRefreshId = UUID.randomUUID().toString();
        final String newRefreshToken = jwtTokenUtil.generateRefreshToken(authentication, newRefreshId);
        storeUserSessionToRedis(user, newRefreshToken);
        addRefreshTokenToCookie(response, newRefreshToken);

        return new AuthTokenDto(newAccessToken);
    }
}
