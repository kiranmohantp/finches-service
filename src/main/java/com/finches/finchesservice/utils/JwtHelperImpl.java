package com.finches.finchesservice.utils;

import com.finches.finchesservice.models.response.JwtUserPayload;
import com.finches.finchesservice.utils.contracts.JwtHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelperImpl implements JwtHelper {
    private static final long JWT_TOKEN_VALIDITY = 5L * 60 * 60;
    private static final String NAME = "userName";

    @Value("${jwt.secret}")
    private String secret;


    @Override
    public String generateToken(JwtUserPayload userDetails) {
        Map<String, Object> claims = new HashMap<>();
        setDetailsToClaimMap(claims, userDetails);
        return doGenerateToken(claims, userDetails.getEncodedId());
    }

    @Override
    public Boolean validateToken(String token, JwtUserPayload userJwtDetails) {
        final String encodedId = getEncodedIdFromToken(token);
        return (encodedId.equals(userJwtDetails.getEncodedId()) && !isTokenExpired(token));
    }

    @Override
    public String getEncodedId(String token) {
        return getEncodedIdFromToken(token);
    }


    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private String getEncodedIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private void setDetailsToClaimMap(Map<String, Object> claims, JwtUserPayload userDetails) {
        claims.put(NAME, userDetails.getUserName());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
