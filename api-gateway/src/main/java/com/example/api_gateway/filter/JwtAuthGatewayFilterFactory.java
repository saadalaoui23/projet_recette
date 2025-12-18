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

    // Liste des endpoints qui ne nécessitent PAS de JWT
    public static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/users/api/users/register",
            "/users/api/users/login",
            "/users/api/users/",  // Pour matcher /users/api/users/{id}/exists
            "exists"              // Contient "exists" quelque part
    );

    public JwtAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public String name() {
        return "JwtAuth";
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            if (request.getMethod().name().equals("OPTIONS")) {
                return chain.filter(exchange);
            }

            // ✅ CORRECTION LOGIQUE : Si le chemin n'est PAS sécurisé (est public), on passe directement
            if (!isSecured(request)) {
                return chain.filter(exchange);
            }

            // 2. Vérifier header Authorization pour les routes sécurisées
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return this.onError(exchange, "Header Authorization manquant ou invalide", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            // 3. Valider le token
            try {
                validateToken(token);

                // 4. Ajouter infos utilisateur (Optionnel : utile pour les services en aval)
                Claims claims = getClaimsFromToken(token);
                exchange = exchange.mutate()
                        .request(request.mutate().header("loggedInUser", claims.getSubject()).build())
                        .build();

            } catch (Exception e) {
                return this.onError(exchange, "Token invalide ou expiré", HttpStatus.UNAUTHORIZED);
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
        // ✅ On vérifie si le chemin actuel contient l'un des mots-clés publics
        return OPEN_API_ENDPOINTS.stream().noneMatch(path::contains);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        // Vous pouvez ajouter un message d'erreur dans le body ici si nécessaire
        return response.setComplete();
    }

    public static class Config {}
}