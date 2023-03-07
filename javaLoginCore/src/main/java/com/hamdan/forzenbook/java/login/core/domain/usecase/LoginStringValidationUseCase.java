package com.hamdan.forzenbook.java.login.core.domain.usecase;

import androidx.annotation.NonNull;

import com.hamdan.forzenbook.java.login.core.domain.LoginEntrys;

public interface LoginStringValidationUseCase {
    @NonNull
    public LoginEntrys invoke(LoginEntrys state);
}
