package com.flabreels.auth.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flabreels.auth.dto.TokenResponseDto;
import com.flabreels.auth.dto.UserDto;
import com.flabreels.auth.entity.Platform;
import com.flabreels.auth.entity.Role;
import com.flabreels.auth.entity.User;
import com.flabreels.auth.entity.UserRepository;
import com.flabreels.auth.jwt.TokenService;
import com.flabreels.auth.mapper.UserRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserRequestMapper userRequestMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserDto userDto = userRequestMapper.toDto(oAuth2User);
        TokenResponseDto tokenResponseDto = tokenService.generateToken(userDto.getId(), Role.USER, Platform.WEB);
        log.info("{}", tokenResponseDto);

        //이메일 있으면 중복 체크해서 DB에 쌓이지 않도록 설정
        User user = userRepository.findUserById(userDto.getId());
        if (user == null){
            userRepository.save(userDto.toEntity(userDto, tokenResponseDto.getRefreshToken()));
        }

        writeTokenResponse(response, tokenResponseDto);
    }
    private void writeTokenResponse(HttpServletResponse response, TokenResponseDto tokenResponseDto)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("access_token", tokenResponseDto.getAccessToken());
        response.addHeader("refresh_token", tokenResponseDto.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(tokenResponseDto));
        writer.flush();
    }


}
