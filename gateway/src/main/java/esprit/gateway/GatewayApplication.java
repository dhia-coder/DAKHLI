package esprit.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Microservice User
                .route("auth-service", r -> r.path("/api/auth/**").uri("lb://user-service"))
                .route("user-service", r -> r.path("/api/users/**").uri("lb://user-service"))
                .route("role-service", r -> r.path("/api/roles/**").uri("lb://user-service"))
                .route("permission-service", r -> r.path("/api/permissions/**").uri("lb://user-service"))
                .route("classe-service", r -> r.path("/api/classes/**").uri("lb://user-service"))
                .route("etablissement-service", r -> r.path("/api/etablissements/**").uri("lb://user-service"))
                .route("discipline-service", r -> r.path("/api/disciplines/**").uri("lb://user-service"))
                .route("region-service", r -> r.path("/api/regions/**").uri("lb://user-service"))
                .route("calendrier-service", r -> r.path("/api/calendar-events/**").uri("lb://calendrier-service"))
                .build();
    }
}
/*
@Configuration
class CorsHeaderCleanupConfig {
    @Bean
    public GlobalFilter corsHeaderCleanupFilter() {
        return (exchange, chain) -> {
            exchange.getResponse().getHeaders().remove("Access-Control-Allow-Origin");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                exchange.getResponse().getHeaders().set("Access-Control-Allow-Origin", "http://localhost:4200");
            }));
        };
    }
}*/


