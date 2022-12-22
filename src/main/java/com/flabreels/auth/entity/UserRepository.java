package com.flabreels.auth.entity;

import com.flabreels.auth.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByRefreshToken(String refreshToken);

    User findUserByEmail(String email);

    User findUserById(String id);
}
