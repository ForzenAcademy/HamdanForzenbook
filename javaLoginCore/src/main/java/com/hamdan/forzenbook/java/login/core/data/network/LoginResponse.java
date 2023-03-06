package com.hamdan.forzenbook.java.login.core.data.network;

import androidx.annotation.Nullable;

public class LoginResponse {
    @Nullable
    final private String token;

    public LoginResponse(@Nullable String token) {
        this.token = token;
    }

    @Nullable
    public String getToken() {
        return token;
    }
}
