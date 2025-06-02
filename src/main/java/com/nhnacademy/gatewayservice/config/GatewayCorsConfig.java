package com.nhnacademy.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayCorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // /api/v1/token/**
        CorsConfiguration tokenConfig = new CorsConfiguration();
        tokenConfig.addAllowedOriginPattern("*");
        tokenConfig.addAllowedMethod("GET");
        tokenConfig.addAllowedMethod("POST");
        tokenConfig.addAllowedMethod("PUT");
        tokenConfig.addAllowedMethod("DELETE");
        tokenConfig.addAllowedHeader("*");
        tokenConfig.setAllowCredentials(true);
        tokenConfig.setMaxAge(3600L);
        source.registerCorsConfiguration("/api/v1/token/**", tokenConfig);

        // /api/v1/members/**
        CorsConfiguration membersConfig = new CorsConfiguration();
        membersConfig.addAllowedOriginPattern("*");
        membersConfig.addAllowedMethod("GET");
        membersConfig.addAllowedMethod("POST");
        membersConfig.addAllowedMethod("PUT");
        membersConfig.addAllowedMethod("DELETE");
        membersConfig.addAllowedHeader("*");
        membersConfig.setAllowCredentials(true);
        membersConfig.setMaxAge(3600L);
        source.registerCorsConfiguration("/api/v1/members/**", membersConfig);

        // /api/v1/attendances/**
        CorsConfiguration attendancesConfig = new CorsConfiguration();
        attendancesConfig.addAllowedOriginPattern("*");
        attendancesConfig.addAllowedMethod("GET");
        attendancesConfig.addAllowedMethod("POST");
        attendancesConfig.addAllowedMethod("PUT");
        attendancesConfig.addAllowedMethod("DELETE");
        attendancesConfig.addAllowedHeader("*");
        attendancesConfig.setAllowCredentials(true);
        attendancesConfig.setMaxAge(3600L);
        source.registerCorsConfiguration("/api/v1/attendances/**", attendancesConfig);

        // /api/v1/books/**
        CorsConfiguration booksConfig = new CorsConfiguration();
        booksConfig.addAllowedOriginPattern("*");
        booksConfig.addAllowedMethod("GET");
        booksConfig.addAllowedMethod("POST");
        booksConfig.addAllowedMethod("PUT");
        booksConfig.addAllowedMethod("DELETE");
        booksConfig.addAllowedHeader("*");
        booksConfig.setAllowCredentials(true);
        booksConfig.setMaxAge(3600L);
        source.registerCorsConfiguration("/api/v1/books/**", booksConfig);

        // /api/v1/chat/**
        CorsConfiguration chatConfig = new CorsConfiguration();
        chatConfig.addAllowedOrigin("http://localhost:10253");
        chatConfig.addAllowedMethod("GET");
        chatConfig.addAllowedMethod("POST");
        chatConfig.addAllowedMethod("PUT");
        chatConfig.addAllowedMethod("DELETE");
        chatConfig.addAllowedHeader("*");
        chatConfig.setAllowCredentials(true);
        chatConfig.setMaxAge(3600L);
        source.registerCorsConfiguration("/api/v1/chat/**", chatConfig);

        // /api/v1/notification/**
        CorsConfiguration notificationConfig = new CorsConfiguration();
        notificationConfig.addAllowedOrigin("http://localhost:10253");
        notificationConfig.addAllowedMethod("GET");
        notificationConfig.addAllowedMethod("POST");
        notificationConfig.addAllowedMethod("PUT");
        notificationConfig.addAllowedMethod("DELETE");
        notificationConfig.addAllowedHeader("*");
        notificationConfig.setAllowCredentials(true);
        notificationConfig.setMaxAge(3600L);
        source.registerCorsConfiguration("/api/v1/notification/**", notificationConfig);

        return new CorsWebFilter(source);
    }
}
