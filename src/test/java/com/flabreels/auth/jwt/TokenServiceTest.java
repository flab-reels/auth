package com.flabreels.auth.jwt;

import com.flabreels.auth.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class TokenServiceTest {

    @DisplayName("토큰 가져와서 유효하면 통과")
    @Test
    public void tokenCheck(){
        // Given
        String email = "godol811@naver.com";
        long accessTokenPeriod = 1000L * 60L * 10L; // 10min
        String secretKey = Base64.getEncoder().encodeToString("f-lab-reels".getBytes());
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", Role.USER);
        Date now = new Date();
        // When
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        // Then
        assertThat(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject()).isEqualTo(email);
    }

}