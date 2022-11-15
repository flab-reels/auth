package com.flabreels.auth.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByRefreshToken(String refreshToken);
    User findUserByEmail(String email);
}
