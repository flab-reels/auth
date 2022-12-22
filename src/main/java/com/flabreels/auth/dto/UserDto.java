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

    @Builder
    public UserDto(String email, String picture, String name, String id) {
        this.email = email;
        this.picture = picture;
        this.name = name;
        this.id = id;
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
