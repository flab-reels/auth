package com.flabreels.auth.dto;

import com.flabreels.auth.entity.Platform;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
public class TokenResponseDto {
    private String id;
    private String accessToken;
    private String refreshToken;
    private String email;
    private String picture;
    private Platform platform;
    //WEB용
    @Builder
    public TokenResponseDto(String id, String accessToken, String refreshToken, String email, String picture, Platform platform) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.picture = picture;
        this.platform = platform;
    }
    //MOBILE용
    @Builder
    public TokenResponseDto(String id, String accessToken, String email, String picture, Platform platform) {
        this.id = id;
        this.accessToken = accessToken;
        this.email = email;
        this.picture = picture;
        this.platform = platform;
    }

}
