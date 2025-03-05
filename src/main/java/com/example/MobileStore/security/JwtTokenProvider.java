package com.example.MobileStore.security;

import com.example.MobileStore.entity.User;
import com.example.MobileStore.exception.InvalidTokenException;
import com.example.MobileStore.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.xml.crypto.Data;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication){
        Map<String,Object>claims=new HashMap<>();
        var user= (UserDetails)authentication.getPrincipal();

        var expirationTime=new Date(System.currentTimeMillis()+jwtExpirationInMs*1000L);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationTime)
                .signWith(getSignKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims,T>claimsResolver) {
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJwt(token)
                    .getBody();
        }catch (ExpiredJwtException e) {
            throw new TokenExpiredException("JWT token is expired");
        }
        catch (Exception e) {
            throw new InvalidTokenException("Invalid JWT token");
        }
    }

    public Boolean validateToken(String token,UserDetails userDetails){
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }


}
