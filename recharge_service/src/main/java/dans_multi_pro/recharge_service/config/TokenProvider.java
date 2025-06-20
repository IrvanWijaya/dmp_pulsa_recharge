package dans_multi_pro.recharge_service.config;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    @Value("${jwt.signing.key}")
    public String SIGNING_KEY;

    @Value("${jwt.authorities.key}")
    public String AUTHORITIES_KEY;

    @Value("${jwt.user.id.key}")
    public String USER_ID_KEY;


    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserIdFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = this.getClaimFromToken(token, c -> c);
        return claims.get(USER_ID_KEY, Long.class);
    }

    public Set<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = getClaimFromToken(token, c -> c);
        String roles = claims.get(AUTHORITIES_KEY, String.class);
        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
