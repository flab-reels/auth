package com.flabreels.auth.jwt;


import com.flabreels.auth.dto.TokenResponseDto;
import com.flabreels.auth.dto.ValidResponseDto;
import com.flabreels.auth.entity.Platform;
import com.flabreels.auth.entity.Role;
import com.flabreels.auth.entity.User;
import com.flabreels.auth.entity.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    final private UserRepository userRepository;
    private @Value("${jwt.secret_key}") String secretKey;
    private long accessTokenPeriod = 1000L * 60L * 10L * 24L; //// 10min
    private long refreshTokenPeriod = 1000L * 60L * 60L * 24L * 30L * 3L; // 3weeks

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public TokenResponseDto generateToken(String uid, Role role, Platform platform){

        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("role", role);
        Date now = new Date();
        // WEB일경우 AccessToken , RefreshToken 반환
        if (platform == Platform.WEB){
            return new TokenResponseDto(
                    Jwts.builder()
                            .setClaims(claims)
                            .setIssuedAt(now)
                            .setExpiration(new Date(now.getTime() + accessTokenPeriod))
                            .signWith(SignatureAlgorithm.HS256, secretKey)
                            .compact(),
                    Jwts.builder()
                            .setClaims(claims)
                            .setIssuedAt(now)
                            .setExpiration(new Date(now.getTime() + refreshTokenPeriod))
                            .signWith(SignatureAlgorithm.HS256, secretKey)
                            .compact()
                    ,uid
                    ,Platform.WEB
            );
        }
        // MOBILE 일경우 AccessToken 만 반환 -> 기간은 RefreshToken 만큼
        return new TokenResponseDto(
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshTokenPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact()
                ,uid
                ,Platform.MOBILE
        );

    }
    // Refresh Token 으로 Access Token 재 발급해주기
    public TokenResponseDto regenerateAccessTokenWithRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader("refresh_token");
        User user = userRepository.findUserByRefreshToken(refreshToken);
        String dbEmail = user.getEmail();
        String dbRefreshToken = user.getRefreshToken();
        log.info("{}",user);
        // Refresh Token이 유효하고 RefreshToken과 일치하다며 이메일이 Database에 존재한다면

        if ((verifyRefreshToken(request)) && (refreshToken.equals(dbRefreshToken)) && (dbEmail != null)){
            Claims claims = Jwts.claims().setSubject(dbEmail);
            claims.put("role", user.getRole());
            Date now = new Date();
            return new TokenResponseDto(
                    Jwts.builder()
                            .setClaims(claims)
                            .setIssuedAt(now)
                            .setExpiration(new Date(now.getTime() + accessTokenPeriod))
                            .signWith(SignatureAlgorithm.HS256, secretKey)
                            .compact()
                    ,dbRefreshToken
                    ,dbEmail
                    ,Platform.WEB
            );

        }
        // 이메일이 유효하고 Access Token 과 Refresh Token 이 만료 되었다면 새로이 Token들을 재발급
        return generateToken(dbEmail,Role.USER,Platform.WEB);
    }

    //토큰 검증
    public boolean verifyAccessToken(HttpServletRequest request){
        String accessToken = request.getHeader("access_token");

        if (accessToken != null){
            try {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
                return true;
            } catch (SignatureException e) {
                log.error("유효하지 않은 서명 토큰", e);
            } catch (MalformedJwtException e) {
                log.error("유효하지 않은 토큰", e);
            } catch (ExpiredJwtException e) {
                log.error("만료된 토큰", e);
            } catch (UnsupportedJwtException e) {
                log.error("지원하지 않는 토큰", e);
            } catch (IllegalArgumentException e) {
                log.error("비어있는 토큰", e);
            }
        }
        return false;
    }

    //토큰 검증
    public boolean verifyRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader("refresh_token");

        if (refreshToken != null){
            try {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
                return true;
            } catch (SignatureException e) {
                log.error("유효하지 않은 서명 토큰", e);
            } catch (MalformedJwtException e) {
                log.error("유효하지 않은 토큰", e);
            } catch (ExpiredJwtException e) {
                log.error("만료된 토큰", e);
            } catch (UnsupportedJwtException e) {
                log.error("지원하지 않는 토큰", e);
            } catch (IllegalArgumentException e) {
                log.error("비어있는 토큰", e);
            }
        }
        return false;
    }

    public ValidResponseDto getEmailAndPicture(String token) {
        String email = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        String picture = userRepository.findUserByEmail(email).getPicture();
        return ValidResponseDto.builder()
                .email(email)
                .picture(picture)
                .build();
    }

}
