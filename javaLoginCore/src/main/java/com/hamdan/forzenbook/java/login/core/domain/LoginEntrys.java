package com.hamdan.forzenbook.java.login.core.domain;

import androidx.annotation.NonNull;

import com.hamdan.forzenbook.java.core.Entry;

public class LoginEntrys {
    final private Entry email;
    final private Entry code;

    public LoginEntrys(Entry inEmail, Entry inCode) {
        email = inEmail;
        code = inCode;
    }

    @NonNull
    public Entry getEmail() {
        return email;
    }

    @NonNull
    public Entry getCode() {
        return code;
    }
}
