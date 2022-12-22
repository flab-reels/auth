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

    public TokenResponseDto generateToken(String id, Role role, Platform platform){

        Claims claims = Jwts.claims().setSubject(id);
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
                    ,id
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
                ,id
                ,Platform.MOBILE
        );

    }
    // Refresh Token 으로 Access Token 재 발급해주기
    public TokenResponseDto regenerateAccessTokenWithRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader("refresh_token");
        User user = userRepository.findUserByRefreshToken(refreshToken);
        String id = user.getId();
        String dbEmail = user.getEmail();
        String dbRefreshToken = user.getRefreshToken();
        log.info("{}",user);
        // Refresh Token이 유효하고 RefreshToken과 일치하다며 ID가 Database에 존재한다면

        if ((verifyRefreshToken(request)) && (refreshToken.equals(dbRefreshToken)) && (id != null)){
            Claims claims = Jwts.claims().setSubject(id);
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
                    ,id
                    ,Platform.WEB
            );

        }
        // ID가 유효하고 Access Token 과 Refresh Token 이 만료 되었다면 새로이 Token들을 재발급
        return generateToken(id,Role.USER,Platform.WEB);
    }

    //토큰 검증
    public boolean verifyAccessToken(HttpServletRequest request){
        String accessToken = request.getHeader("access_token");
        return tokenExceptionHandler(accessToken);
    }

    //토큰 검증
    public boolean verifyRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader("refresh_token");
        return tokenExceptionHandler(refreshToken);
    }

    public boolean tokenExceptionHandler(String token){
        if (token != null){
            try {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
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

    public ValidResponseDto getIdAndPicture(String token) {
        String id = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        String picture = userRepository.findUserById(id).getPicture();
        return ValidResponseDto.builder()
                .id(id)
                .picture(picture)
                .build();
    }

}
