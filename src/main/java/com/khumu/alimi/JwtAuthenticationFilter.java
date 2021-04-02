package com.khumu.alimi;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.khumu.alimi.data.entity.SimpleKhumuUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    @Value("${jwt.secret}")
    private final String secret = null;
    private final JWTVerifier jwtVerifier;

    public String getUsernameFromToken(String token) {

        DecodedJWT t = jwtVerifier.verify(token);
        String username = t.getClaim("user_id").asString();
        return username;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authHeaderString = ((HttpServletRequest) request).getHeader("Authorization");
        logger.info("Authorization :" + authHeaderString);
        String tokenString = null;
        try {
            if (authHeaderString != null && authHeaderString.startsWith("Bearer ")) {
                tokenString = authHeaderString.substring("Bearer ".length());
                logger.info(tokenString);
            }

            if (tokenString != null) {
                String username = getUsernameFromToken(tokenString);
                logger.info("Requesting username in: " + username);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(SimpleKhumuUser.builder().username(username).build(), "", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Request user가 null인 채로 요청 진행");
        }
        // doFilter called twice..?
        chain.doFilter(request, response);
    }
}
