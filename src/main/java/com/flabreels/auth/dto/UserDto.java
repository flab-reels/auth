package com.flabreels.auth.dto;

import com.flabreels.auth.entity.Platform;
import com.flabreels.auth.entity.Role;
import com.flabreels.auth.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {
    private String email;
    private String picture;
    private String name;
    private String id;
    private Role role;
    private Platform platform;
    private String refreshToken;


    @Builder
    public UserDto(String email, String picture, String name, String id, String refreshToken, Role role, Platform platform) {
        this.email = email;
        this.picture = picture;
        this.name = name;
        this.id = id;
        this.refreshToken = refreshToken;
        this.role = role;
        this.platform = platform;
    }




    public User toEntity(UserDto userDto, String refreshToken){
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .refreshToken(refreshToken)
                .picture(userDto.getPicture())
                .role(Role.USER)
                .platform(Platform.WEB)
                .build();
    }


}
