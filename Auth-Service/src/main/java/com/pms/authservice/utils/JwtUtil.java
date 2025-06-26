package com.pms.authservice.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Utility class for handling JSON Web Token (JWT) operations.
 * <p>
 * This class provides methods for generating JWT tokens using a secret key.
 * The secret key is injected from the application properties and used to sign the tokens.
 * </p>
 *
 * <p>
 * <b>Usage:</b> Inject this component into services or controllers that require JWT generation.
 * </p>
 *
 * <p>
 * <b>Configuration:</b> The secret key should be provided in the application properties as {@code jwt.secret},
 * and must be a Base64-encoded string.
 * </p>
 *
 * @author Denis Kinyua
 * @since 1.0
 */
@Component
public class JwtUtil {

    /**
     * The secret key used for signing JWT tokens.
     */
    private final Key secretKey;

    /**
     * Constructs a new {@code JwtUtil} instance with the provided secret key.
     *
     * @param secret the Base64-encoded secret key from application properties
     */
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token containing the user's email and role as claims.
     *
     * @param email the user's email to be set as the subject of the token
     * @param role  the user's role to be included as a claim
     * @return a signed JWT token as a {@link String}
     */
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(secretKey)
                .compact();
    }

    public void validateToken(String jwtToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(jwtToken);
        } catch (SignatureException signatureException){
            throw new JwtException("Invalid JWT Signature");
        } catch (JwtException jwtException){
            throw  new JwtException("Invalid JWT Token");
        }

    }
}