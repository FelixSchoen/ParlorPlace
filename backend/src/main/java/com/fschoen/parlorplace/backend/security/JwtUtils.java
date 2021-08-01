package com.fschoen.parlorplace.backend.security;

import com.fschoen.parlorplace.backend.entity.transience.*;
import com.fschoen.parlorplace.backend.utility.messaging.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.crypto.*;
import java.nio.charset.*;
import java.util.*;
import java.lang.SecurityException;

@Slf4j
@Component
public class JwtUtils {

    @Value("${fschoen.parlorplace.jwtSecret}")
    private String jwtSecret;

    @Value("${fschoen.parlorplace.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(UserDetailsImplementation userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(getKey(jwtSecret))
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey(jwtSecret)).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey(jwtSecret)).build().parseClaimsJws(authToken);
            return true;
        } catch (SecurityException e) {
            log.error(Messages.exception("authorization.signature.invalid"), e.getMessage());
        } catch (MalformedJwtException e) {
            log.error(Messages.exception("authorization.token.invalid"), e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error(Messages.exception("authorization.token.expired"), e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error(Messages.exception("authorization.token.unsupported"), e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(Messages.exception("authorization.token.empty"), e.getMessage());
        }

        return false;
    }

    private static SecretKey getKey(String jwtSecret) {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

}
