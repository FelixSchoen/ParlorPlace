package com.fschoen.parlorplace.backend.security;

import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${fschoen.parlorplace.jwtSecret}")
    private String jwtSecret;

    @Value("${fschoen.parlorplace.jwtExpirationMs}")
    private int jwtExpirationMs;

    private static SecretKey getKey(String jwtSecret) {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

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
            log.error(Messages.exception(MessageIdentifier.AUTHORIZATION_SIGNATURE_INVALID), e);
        } catch (MalformedJwtException e) {
            log.error(Messages.exception(MessageIdentifier.AUTHORIZATION_TOKEN_INVALID), e);
        } catch (ExpiredJwtException e) {
            log.error(Messages.exception(MessageIdentifier.AUTHORIZATION_TOKEN_EXPIRED), e);
        } catch (UnsupportedJwtException e) {
            log.error(Messages.exception(MessageIdentifier.AUTHORIZATION_TOKEN_UNSUPPORTED), e);
        } catch (IllegalArgumentException e) {
            log.error(Messages.exception(MessageIdentifier.AUTHORIZATION_TOKEN_EMPTY), e);
        }

        return false;
    }

}
