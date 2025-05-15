package com.nhnacademy.gatewayservice.filter;

import com.nhnacademy.gatewayservice.exception.TokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Config {
        private String secretKey;
    }

    public JwtAuthenticationFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        String secretKey = config.secretKey;

        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            String accessToken = extractTokenFromRequest(request);
            if(accessToken == null){
                response.setStatusCode(HttpStatus.UNAUTHORIZED); //d여기 걸림 !
                return response.setComplete();
            }

            return Mono.defer(() -> {
                        try {
                            PublicKey key = getPublicKey(secretKey);
                            return validateToken(key, accessToken);
                        } catch (Exception e) {
                            return Mono.error(new TokenException("공개키 파싱 실패"));
                        }
                    })
                    .flatMap(email -> {
                        ServerWebExchange serverWebExchange = exchange.mutate()
                                .request(r -> r.header("X-USER", email)) //이거 써야함 안스면 401 error
                                .build();
                        return chain.filter(serverWebExchange);
                    })
                    .onErrorResume(TokenException.class, e -> {
                        log.warn("토큰 검증 실패: {}", e.getMessage());
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return response.setComplete();
                    });
        });
    }


    public Mono<String> validateToken(PublicKey key, String token) {
        try {
            String email = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("email").toString();
            return Mono.just(email);
        } catch (ExpiredJwtException e) {
            throw new TokenException("인증 토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            throw new TokenException("지원하지 않는 인증 토큰입니다.");
        } catch (MalformedJwtException e) {
            throw new TokenException("잘못된 인증 토큰 형식입니다");
        } catch (SecurityException e) {
            throw new TokenException("인증 토큰의 서명이 유효하지 않습니다.");
        } catch (IllegalArgumentException e) {
            throw new TokenException("인증 토큰의 형태가 유효하지 않습니다.");
        }
    }

    public String extractTokenFromRequest(ServerHttpRequest request){
//        String token = request.getHeaders().getFirst("Authentication");
//        if(token != null && token.startsWith("Bearer ")){
//            return token.substring(7);
//        }
        HttpCookie tokenCookie = request.getCookies().getFirst("accessToken");
        if (tokenCookie != null) {
            return tokenCookie.getValue();
        }

        return null;
    }

    public PublicKey getPublicKey (String key) throws Exception {
        String publicKey = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] encoded = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }


}
