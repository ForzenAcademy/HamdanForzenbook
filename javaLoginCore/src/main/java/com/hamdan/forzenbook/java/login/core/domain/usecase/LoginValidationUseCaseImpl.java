package com.hamdan.forzenbook.java.login.core.domain.usecase;

import com.hamdan.forzenbook.java.login.core.data.repository.LoginRepository;

public class LoginValidationUseCaseImpl implements LoginValidationUseCase {

    final private LoginRepository repository;

    public LoginValidationUseCaseImpl(LoginRepository repository) {
        this.repository = repository;
    }

    @Override
    public void invoke(String email) {
        repository.requestValidation(email);
    }
}
