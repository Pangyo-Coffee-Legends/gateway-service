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
                .route("token-service", r -> r
                        .path("/api/v1/token/**") // path를 확인하고 uri로 이동
                        .uri("lb://token-service"))
                .route("member-service", r -> r
                        .path("/api/v1/members/**")
                        .uri("lb://member-service")
                ).build();
    }
}
