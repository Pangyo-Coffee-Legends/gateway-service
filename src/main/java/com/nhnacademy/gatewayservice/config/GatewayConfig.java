package com.nhnacademy.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/v1/auth/**") // path를 확인하고 uri로 이동
                        .uri("lb://auth-service"))
                .route("member-service", r -> r
                        .path("/api/v1/members/**")
                        .uri("lb://member-service")
                ).build();
    }
}
