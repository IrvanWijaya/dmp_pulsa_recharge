package dans_multi_pro.api_gateway.config;

import dans_multi_pro.api_gateway.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${user-service-url}")
    private String userServiceUrl;

    @Value("${recharge-service-url}")
    private String rechargeServiceUrl;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {

        System.out.println("============================================================================================");
        System.out.println(userServiceUrl);
        System.out.println(rechargeServiceUrl);
        System.out.println("============================================================================================");

        return builder.routes()
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f.filter(jwtAuthFilter))
                        .uri(userServiceUrl))
                .route("recharge-service", r -> r.path("/recharge/**", "/recharge-webhook/**")
                        .filters(f -> f.filter(jwtAuthFilter)) // optional: apply same auth filter if needed
                        .uri(rechargeServiceUrl))
                .build();
    }
}