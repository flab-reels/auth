package com.flabreels.auth.controller;


import com.flabreels.auth.dto.TokenResponseDto;
import com.flabreels.auth.dto.ValidResponseDto;
import com.flabreels.auth.entity.UserRepository;
import com.flabreels.auth.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    final private TokenService tokenService;
    final private UserRepository userRepository;

    @GetMapping("/access")
    public ResponseEntity<ValidResponseDto> validToken(HttpServletRequest request, HttpServletResponse response){
        String accessToken = request.getHeader("access_token");
        String refreshToken = request.getHeader("refresh_token");
        boolean verifyAccessToken = tokenService.verifyToken(accessToken);

        if (verifyAccessToken){
            response.addHeader("access_token", accessToken);
            response.addHeader("refresh_token", refreshToken);
            response.setContentType("application/json;charset=UTF-8");
            return ResponseEntity.ok(tokenService.getEmailAndPicture(accessToken));
        }
        return (ResponseEntity<ValidResponseDto>) ResponseEntity.status(HttpStatus.NOT_FOUND);


    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response){
        String accessToken = request.getHeader("access_token");
        String refreshToken = request.getHeader("refresh_token");
        boolean verifyRefreshToken = tokenService.verifyToken(refreshToken);
        if (verifyRefreshToken){
            TokenResponseDto tokenResponseDto = tokenService.regenerateAccessTokenWithRefreshToken(refreshToken);
            response.addHeader("access_token", tokenResponseDto.getAccessToken());
            response.addHeader("refresh_token", refreshToken);
            response.setContentType("application/json;charset=UTF-8");
            return ResponseEntity.ok(tokenResponseDto);
        }
        return (ResponseEntity<TokenResponseDto>) ResponseEntity.status(HttpStatus.NOT_FOUND);

    }




}
