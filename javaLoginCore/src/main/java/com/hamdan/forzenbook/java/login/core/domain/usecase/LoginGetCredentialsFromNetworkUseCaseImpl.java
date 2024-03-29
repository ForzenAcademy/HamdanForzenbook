package com.hamdan.forzenbook.java.login.core.domain.usecase;

import com.hamdan.forzenbook.java.login.core.data.repository.FailTokenRetrievalException;
import com.hamdan.forzenbook.java.login.core.data.repository.LoginRepository;
import com.hamdan.forzenbook.java.login.core.data.repository.NullTokenException;
import com.hamdan.forzenbook.java.login.core.data.repository.User;

import java.io.IOException;

public class LoginGetCredentialsFromNetworkUseCaseImpl implements LoginGetCredentialsFromNetworkUseCase {

    final private LoginRepository repository;

    public LoginGetCredentialsFromNetworkUseCaseImpl(LoginRepository repository) {
        this.repository = repository;
    }

    @Override
    public void invoke(String email, String code) throws NullTokenException, FailTokenRetrievalException, IOException {
        User userState = repository.getToken(email, code);
        if (userState != User.LOGGED_IN) {
            throw new NullTokenException("null token");
        }
    }
}
