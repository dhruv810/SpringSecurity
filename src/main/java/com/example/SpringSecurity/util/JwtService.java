package com.example.SpringSecurity.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private String secretKey = "";

    public JwtService() {
        try {
            // using key generator to generate secret key
            KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = generator.generateKey();
            // encoding secret key into string
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    // Generates jwt token using username
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)  // setting username as subject
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30 * 10))
                .and()
                .signWith(getKey())             // generate key for this
                .compact();
    }

    private SecretKey getKey() {
        // decoding secret key
        byte[] byteCode = Base64.getDecoder().decode(secretKey) ;
        // generating key using secret key
        return Keys.hmacShaKeyFor(byteCode);
    }

    // extracts username from token
    public String extractUsername(String token) {
        // extracting claims from token and getting subject/username
        // passing in method as Functional interface method
        System.out.println("extracting username");
        return extractClaim(token, Claims::getSubject);
    }

    // extract all claims from token and runs Functional interface method
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        // get all claims
        final Claims claims = extractAllClaims(token);
        // using functional interface to perform operation that is passed in
        return claimResolver.apply(claims);
    }

    // extract all claims from token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // validates token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return userDetails.getUsername().equals(username) && !isTokenExpired(token);
    }

    // validates expiration date
    private boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    // extracts expiration date from token
    private Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
