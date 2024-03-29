package com.backend.starter.security.jwt;


import com.backend.starter.serviceImpl.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    public static final long TOKEN_EXPIRATION_TIME=1000*60*60*24;
    private static final String APPLICATION_SECRET_KEY = "spring-boot-starter-backend";

    public String generateToken(Authentication authentication){

        UserDetailsImpl userDetails =(UserDetailsImpl)authentication.getPrincipal();

        Map<String,Object> claims = new HashMap<String,Object>();
        claims.put("roles",userDetails.getAuthorities());

        System.out.println("Line:29: JwtUtil class generateToken method");
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, APPLICATION_SECRET_KEY)
                .compact();


    }

    public String getUserNameFromJwtToken(String token) {
        System.out.println("Line:43: JwtUtil class getUserNameFromJwtToken method");

        return Jwts.parser().setSigningKey(APPLICATION_SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        System.out.println("Line:29: JwtUtil class validateJwtToken method");

        try {
            Jwts.parser().setSigningKey(APPLICATION_SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }








}
