package com.flabreels.auth.mapper;

import com.flabreels.auth.dto.UserDto;
import com.flabreels.auth.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class UserRequestMapper {
    public UserDto toDto(OAuth2User oAuth2User){
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return UserDto.builder()
                .id(UUID.randomUUID().toString())
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .picture((String) attributes.get("picture"))
                .build();
    }

    public UserDto toDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .picture(user.getPicture())
                .build();
    }
}
