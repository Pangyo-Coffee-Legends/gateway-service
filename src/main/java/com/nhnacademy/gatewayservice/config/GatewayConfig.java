package com.nhnacademy.gatewayservice.config;

import com.nhnacademy.gatewayservice.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${jwt.secret}")
    private String key;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, JwtAuthenticationFilter jwtAuthenticationFilter) {
        return builder.routes()
                .route("token-service", r -> r
                        .path("/api/v1/token/**")
                        .uri("lb://token-service"))
                .route("member-service", r -> r
                        .path("/api/v1/members/**")
                        .uri("lb://member-service"))
                .route("meeting-room-service", r -> r
                        .path("/api/v1/meeting-rooms/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))
                        .uri("lb://meeting-room-service"))
                .route("work-entry-service", r -> r
                        .path("/api/v1/attendances/**", "/api/v1/entries/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))// work-entry-service API요청 허용
                        .uri("lb://work-entry-service"))
                .route("analysis-service", r -> r
                        .path("/api/v1/analysis/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))// analysis-service API요청 허용
                        .uri("lb://analysis-service"))
                .route("booking-service", r -> r
                        .path("/api/v1/bookings/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))
                        .uri("lb://booking-service"))
                .build();
    }
}
