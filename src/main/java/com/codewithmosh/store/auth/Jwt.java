package com.codewithmosh.store.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;

import javax.crypto.SecretKey;

import com.codewithmosh.store.users.Role;

public class Jwt {
    private Claims claims;
    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public Long getUserId() {
        return Long.parseLong(claims.getSubject());
    }

    public Role getRole() {
        return Role.valueOf(claims.get("role").toString());
    }

    public String toString() {
        return Jwts.builder()
            .claims(claims)
            .signWith(secretKey)
            .compact();
    }
}
