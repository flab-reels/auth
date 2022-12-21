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

    @GetMapping("/authorize")
    public String validEmail(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("email", request.getHeader("email"));
        return request.getHeader("email");
    }

    @GetMapping("/access")
    public ResponseEntity<ValidResponseDto> validToken(HttpServletRequest request, HttpServletResponse response){

        boolean verifyAccessToken = tokenService.verifyAccessToken(request);
        if (verifyAccessToken){
            response.addHeader("access_token", request.getHeader("access_token"));
            response.addHeader("refresh_token", request.getHeader("refresh_token"));
            response.setContentType("application/json;charset=UTF-8");
            return ResponseEntity.ok(tokenService.getEmailAndPicture(request.getHeader("access_token")));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response){

        boolean verifyRefreshToken = tokenService.verifyRefreshToken(request);
        if (verifyRefreshToken){
            TokenResponseDto tokenResponseDto = tokenService.regenerateAccessTokenWithRefreshToken(request);
            response.addHeader("access_token", tokenResponseDto.getAccessToken());
            response.addHeader("refresh_token", request.getHeader("refresh_token"));
            response.setContentType("application/json;charset=UTF-8");
            return ResponseEntity.ok(tokenResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }




}
