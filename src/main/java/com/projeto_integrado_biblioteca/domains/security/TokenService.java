package com.projeto_integrado_biblioteca.domains.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.projeto_integrado_biblioteca.domains.user.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Value("${jwt.secret.key}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    private Algorithm algorithm = null;

    @PostConstruct
    protected void init() {
        algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(User user) {
        try {
            Instant issueAt = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant();
            Instant expiresAt = issueAt.plus(Duration.ofMinutes(60));
            String scopes = user
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            return JWT
                    .create()
                    .withIssuer(issuer)
                    .withIssuedAt(issueAt)
                    .withExpiresAt(expiresAt)
                    .withSubject(user.getUserId().toString())
                    .withClaim("email", user.getEmail())
                    .withClaim("scopes", scopes)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            return null;
        }
    }

    public String validateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT
                    .require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);

            return decodedJWT.getClaim("email").asString();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
