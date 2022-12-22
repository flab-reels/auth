package com.flabreels.auth.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ValidResponseDto {
    private String email;
    private String picture;
    private String id;
    @Builder
    public ValidResponseDto(String email, String picture, String id) {
        this.email = email;
        this.picture = picture;
        this.id = id;
    }
}
