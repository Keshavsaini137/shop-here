package com.shop_here.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
//    private static final long EXPIRATION_MS=86400000;
//    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


    public String generateToken(UserDetails userDetails){
//        Map<String,Object> claims = new HashMap<>();
        System.out.println("Inside Generate Token : " +userDetails.getAuthorities() );
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return createToken(role, userDetails.getUsername());
    }

    public String createToken(String role, String username){

       return Jwts.builder()
               .setSubject(username)
               .claim("role",role)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
               .signWith(getSigningKey(),SignatureAlgorithm.HS256)
               .compact();
    }

//    public String extractUsername(String token){
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }


    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
