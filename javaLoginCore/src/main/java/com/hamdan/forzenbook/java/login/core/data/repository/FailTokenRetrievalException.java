package com.hamdan.forzenbook.java.login.core.data.repository;

import androidx.annotation.NonNull;

public class FailTokenRetrievalException extends Exception {
    private final String message;

    @NonNull
    public String getMessage() {
        return message;
    }

    public FailTokenRetrievalException(String message) {
        this.message = message;
    }
}