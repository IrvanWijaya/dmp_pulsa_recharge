package dans_multi_pro.recharge_service.common.aop.annotation;

import dans_multi_pro.recharge_service.config.TokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;
import java.util.stream.Collectors;


@Component
public class RequireRoleInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod handlerMethod) {
            RequireRole roleAnnotation = handlerMethod.getMethodAnnotation(RequireRole.class);
            if (roleAnnotation != null) {
                String authHeader = request.getHeader("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing or invalid Authorization header");
                    return false;
                }

                String token = authHeader.substring(7);
                try {
                    Set<GrantedAuthority> tokenAuthorities = tokenProvider.getAuthoritiesFromToken(token);
                    Set<GrantedAuthority> grantedAuthorities = tokenAuthorities.stream()
                            .filter(ga -> ga.getAuthority().contains(roleAnnotation.value().name())) //!TODO this is can be improved because role name can be simmilar
                            .collect(Collectors.toSet());
                    if (grantedAuthorities.isEmpty()) {
                        response.sendError(HttpStatus.FORBIDDEN.value(),
                                "Forbidden: Requires role " + roleAnnotation.value().name());
                        return false;
                    }

                } catch (Exception e) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired token");
                    return false;
                }
            }
        }

        return true;
    }
}