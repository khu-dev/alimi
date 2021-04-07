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

    public String issueToken(String userId, Collection<? extends GrantedAuthority> authorities) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("user_id", userId);
        claims.put("roles", authorities.stream().map(role -> role.getAuthority()).collect(Collectors.toList()));

        LocalDateTime currentTime = LocalDateTime.now();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("battlepang")
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plusYears(100)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        Jws<Claims> claimsJws = null;
        try {
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
