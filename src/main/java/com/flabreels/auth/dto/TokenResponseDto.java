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

    @Builder
    public TokenResponseDto(String id, String accessToken, String refreshToken, String email, String picture, Platform platform) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.picture = picture;
        this.platform = platform;
    }



    @Builder
    public TokenResponseDto(String id, String accessToken, Platform platform) {
        this.id = id;
        this.accessToken = accessToken;
        this.platform = platform;
    }


    @Builder
    public TokenResponseDto(String id, String accessToken, String refreshToken, String email, Platform platform) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.platform = platform;
    }



    @Builder
    public TokenResponseDto(String id, String accessToken, String email,  Platform platform) {
        this.id = id;
        this.accessToken = accessToken;
        this.email = email;
        this.platform = platform;
    }

    @Builder
    public TokenResponseDto(String id, String email, String picture) {
        this.id = id;
        this.email = email;
        this.picture = picture;
    }


}
