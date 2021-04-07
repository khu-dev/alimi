package com.khumu.alimi.auth;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtil {
    @Value("${jwt.secret}")
    String jwtSecret;

    public Jws<Claims> parseToken(String token) {
        Jws<Claims> claimsJws = null;
        try {
            System.out.println(token);
            claimsJws = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build().parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            throw expiredEx;
        }
        return claimsJws;
    }
}
