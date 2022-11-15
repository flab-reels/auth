package com.flabreels.auth.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Platform {


    MOBILE("MOBILE"),
    WEB("WEB");

    private final String value;
}
