package com.hamdan.forzenbook.java.createaccount.core.data.repository;

import androidx.annotation.NonNull;

public class AccountException extends Exception {
    private final String message;

    @NonNull
    public String getMessage() {
        return message;
    }

    public AccountException(String message) {
        this.message = message;
    }
}
