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
                .route("rule-service-rules", r -> r
                        .path("/api/v1/rules/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))
                        .uri("lb://rule-engine-service"))
                .route("rule-service-rule-groups", r -> r
                        .path("/api/v1/rule-groups/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))
                        .uri("lb://rule-engine-service"))
                .route("rule-service-engine", r -> r
                        .path("/api/v1/rule-engine/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))
                        .uri("lb://rule-engine-service"))


                //Action, Condition 추가
                .route("rule-service-actions", r -> r
                        .path("/api/v1/actions/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))
                        .uri("lb://rule-engine-service"))

                .route("rule-service-conditions", r -> r
                        .path("/api/v1/conditions/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))
                        .uri("lb://rule-engine-service"))

                .route("iot-service-sensors", r -> r
                        .path("/api/v1/sensors/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))
                        .uri("lb://iot-service"))

                .route("image-service-places", r -> r
                        .path("/api/v1/places/**")
                        .uri("lb://image-service"))

                .route("iot-service-images", r -> r
                        .path("/images/**")
                        .uri("lb://image-service"))

                .route("rule-service-comfort", r -> r
                        .path("/api/v1/comfort/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))
                        .uri("lb://rule-engine-service"))
                // ✅ [1] Chat REST API - CORS 허용 필요
                .route("chat-service-api", r -> r
                        .path("/api/v1/chat/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))
                        .uri("lb://chat-service"))

                // ✅ [2] Chat WebSocket - CORS 제거, WebSocket 경로 따로
                .route("chat-service-ws", r -> r
                        .path("/ws/chat/connect/**")
                        .uri("lb:ws://chat-service"))

                // ✅ [3] Notification REST API - CORS 허용 필요
                .route("notification-service-api", r -> r
                        .path("/api/v1/notification/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(
                                new JwtAuthenticationFilter.Config() {{
                                    setSecretKey(key);
                                }}
                        )))
                        .uri("lb://notify-service"))

                // ✅ [4] Notification WebSocket - CORS 제거, WebSocket 경로 따로
                .route("notification-service-ws", r -> r
                        .path("/ws/notification/connect/**")
                        .uri("lb:ws://notify-service"))

                .build();
    }
}
