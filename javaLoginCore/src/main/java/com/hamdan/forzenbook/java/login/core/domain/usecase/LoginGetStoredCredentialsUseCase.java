package com.hamdan.forzenbook.java.login.core.domain.usecase;

import com.hamdan.forzenbook.java.login.core.data.repository.FailTokenRetrievalException;
import com.hamdan.forzenbook.java.login.core.data.repository.NullTokenException;

public interface LoginGetStoredCredentialsUseCase {
    public void invoke() throws NullTokenException, FailTokenRetrievalException;
}
