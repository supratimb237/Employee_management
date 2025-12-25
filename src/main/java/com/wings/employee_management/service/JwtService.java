package com.wings.employee_management.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtService {

    //@Value("${jwt.secret}")
    String secretKey = "MySuperSecretKey12345MySuperSecretKey12345MySuperSecretKey12345";

    //@Value("${jwt.expiration}")
    long expTime = 3600000l;

    public String generateToken(String username){
        return Jwts.builder().signWith(SignatureAlgorithm.HS256,secretKey)
                .addClaims(new HashMap<>())
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expTime))
                .compact();
    }

    public Claims getClaims(String token){
        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    public String getUsername(String token){
        return getClaims(token).getSubject();
    }

    public Date getExpiration(String token){
        return getClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token){
        return getClaims(token).getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String name = getUsername(token);
        return (name.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
