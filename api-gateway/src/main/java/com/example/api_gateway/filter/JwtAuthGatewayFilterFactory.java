package com.example.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;

@Component
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config> {

    @Value("${JWT_SECRET_KEY}")
    private String jwtSecret;

    public static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/users/api/users/register",
            "/users/api/users/login",
            "/users/api/users/*/exists"
    );

    public JwtAuthGatewayFilterFactory() {
        super(Config.class);
    }

    // ✅ AJOUT CRUCIAL : On force le nom du filtre pour qu'il corresponde au YAML
    @Override
    public String name() {
        return "JwtAuth";
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. Contourner les endpoints publics
            if (isSecured(request)) {
                return chain.filter(exchange);
            }

            // 2. Vérifier header Authorization
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null) {
                return this.onError(exchange, "Header Authorization manquant", HttpStatus.UNAUTHORIZED);
            }

            if (!authHeader.startsWith("Bearer ")) {
                return this.onError(exchange, "Format JWT invalide", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            // 3. Valider le token
            try {
                validateToken(token);
            } catch (Exception e) {
                return this.onError(exchange, "Token invalide ou expiré", HttpStatus.UNAUTHORIZED);
            }

            // 4. Ajouter infos utilisateur
            try {
                Claims claims = getClaimsFromToken(token);
                exchange.getRequest().mutate().header("username", claims.getSubject()).build();
            } catch (Exception e) {
                // Ignore
            }

            return chain.filter(exchange);
        };
    }

    private void validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isSecured(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        return OPEN_API_ENDPOINTS.stream().noneMatch(path::startsWith);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public static class Config {}
}