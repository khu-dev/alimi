package com.khumu.alimi.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.auth0.jwt.JWT.require;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class JwtServiceImpl {

    private final JWTVerifier jwtVerifier;

    public String getUsernameFromAuthHeader(String authHeader) {
        String jwtStr = getTokenFromAuthHeader(authHeader);
        System.out.println(authHeader);
        System.out.println(jwtStr);
        DecodedJWT t = jwtVerifier.verify(jwtStr);;
        String username = t.getClaim("user_id").asString();
        return username;
    }

    public String getTokenFromAuthHeader(String authHeader) {
        return Arrays.stream(authHeader.split(" ")).collect(Collectors.toList()).get(1);
    }
}
