package com.hamdan.forzenbook.java.login.core.domain.usecase;

import android.util.Log;

import com.hamdan.forzenbook.java.login.core.data.repository.LoginRepository;

import java.io.IOException;

public class LoginValidationUseCaseImpl implements LoginValidationUseCase {

    final private LoginRepository repository;

    public LoginValidationUseCaseImpl(LoginRepository repository) {
        this.repository = repository;
    }

    @Override
    public void invoke(String email) throws IOException {
        repository.requestValidation(email);
    }
}
