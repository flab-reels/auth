package com.flabreels.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "`user`")
public class User {

    // AI -> UUID로 변경해서 올릴 것
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "picture")
    private String picture;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(Long id, String email, String name, String picture, String refreshToken, Platform platform, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.refreshToken = refreshToken;
        this.platform = platform;
        this.role = role;
    }
}
