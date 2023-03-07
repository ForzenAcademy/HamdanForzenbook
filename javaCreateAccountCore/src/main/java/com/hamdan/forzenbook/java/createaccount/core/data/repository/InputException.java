package com.hamdan.forzenbook.java.createaccount.core.data.repository;

import androidx.annotation.NonNull;

public class InputException extends Exception {
    private String message;

    public InputException(String message) {
        this.message = message;
    }

    @NonNull
    public String getMessage(){
        return message;
    }
}