package com.nhnacademy.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayCorsConfig {

    private CorsConfiguration createDefaultCorsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("https://aiot2.live");
        config.addAllowedOrigin("https://www.aiot2.live");
        config.addAllowedOrigin("http://localhost:10253");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        return config;
    }

    /*
        '/**' 경로 전체를 CORS 필터에 등록하면 WebSocket handshake 요청도 필터링 대상이 되어
        연결 실패가 발생할 수 있다.

        WebSocket handshake는 STOMP 프로토콜을 사용해 서버와 연결할 때 이루어지는
        HTTP GET + Upgrade 헤더 기반의 요청인데, 이를 일반 HTTP 요청처럼 처리하게 되면
        CORS 필터가 Origin, Method, Header 등을 검사하다가 차단할 수 있다.

        따라서 WebSocket 경로는 필터 대상에서 제외하고,
        실제 REST API 요청에 대해서만 CORS 설정을 명시적으로 적용한다.
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 모든 Rest API 경로에 동일한 CORS 설정 적용
        source.registerCorsConfiguration("/api/v1/token/**", createDefaultCorsConfig());
        source.registerCorsConfiguration("/api/v1/members/**", createDefaultCorsConfig());
        source.registerCorsConfiguration("/api/v1/attendances/**", createDefaultCorsConfig());
        source.registerCorsConfiguration("/api/v1/books/**", createDefaultCorsConfig());
        source.registerCorsConfiguration("/api/v1/chat/**", createDefaultCorsConfig());
        source.registerCorsConfiguration("/api/v1/notification/**", createDefaultCorsConfig());

        return new CorsWebFilter(source);
    }
}
