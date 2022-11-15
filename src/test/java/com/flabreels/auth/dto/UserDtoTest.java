package com.flabreels.auth.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    @DisplayName("롬복 테스트")
    @Test
    public void lombokTest(){
        // Given
        String email = "godol811@naver.com";
        String picture = "http://picture.org";
        String name = "godol";
        // When
        UserDto userDto = UserDto.builder()
                .name(name)
                .picture(picture)
                .email(email)
                .build();
        // Then
        assertThat(userDto.getEmail()).isEqualTo(email);
        assertThat(userDto.getName()).isEqualTo(name);
        assertThat(userDto.getPicture()).isEqualTo(picture);
    }
}