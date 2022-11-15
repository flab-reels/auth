package com.flabreels.auth.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ValidResponseDto {
    private String email;
    private String picture;

    @Builder
    public ValidResponseDto(String email, String picture) {
        this.email = email;
        this.picture = picture;
    }
}
