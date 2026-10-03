package com.example.jwt.internal;

// Example of a simple JWT generator (for demonstration, use a robust library like JJWT in production)
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

// Updated for JJWT 0.12+: Keys.secretKeyFor(SignatureAlgorithm.HS256), setSubject(), setIssuedAt() and
// setExpiration() are deprecated; the key is typed as SecretKey so NimbusJwtDecoder.withSecretKey() accepts it.
public class JwtGenerator {
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    public String generateToken(String subject, String role) {
        return Jwts.builder()
                .subject(subject)
                .claim("roles", role) // "roles" matches what ProtectedController reads
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 hour expiration
                .signWith(secretKey)
                .compact();
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}
