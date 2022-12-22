package com.flabreels.auth.entity;

import com.flabreels.auth.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByRefreshToken(String refreshToken);
    User findUserByEmail(String email);
    User findUserById(String id);
}
