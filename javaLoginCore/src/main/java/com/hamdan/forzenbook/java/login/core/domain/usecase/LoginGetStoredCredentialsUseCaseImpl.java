package com.hamdan.forzenbook.java.login.core.domain.usecase;

import com.hamdan.forzenbook.java.login.core.data.repository.FailTokenRetrievalException;
import com.hamdan.forzenbook.java.login.core.data.repository.LoginRepository;
import com.hamdan.forzenbook.java.login.core.data.repository.NullTokenException;
import com.hamdan.forzenbook.java.login.core.data.repository.User;

import java.io.IOException;

public class LoginGetStoredCredentialsUseCaseImpl implements LoginGetStoredCredentialsUseCase {

    final private LoginRepository repository;

    public LoginGetStoredCredentialsUseCaseImpl(LoginRepository repository) {
        this.repository = repository;
    }

    @Override
    public void invoke() throws NullTokenException, FailTokenRetrievalException, IOException {
        User state = repository.getToken(null,null);
        if (state == User.LOGGED_IN) {
            // TODO logged in path when homepage implemented
        } else throw new NullTokenException("null token");
    }
}
