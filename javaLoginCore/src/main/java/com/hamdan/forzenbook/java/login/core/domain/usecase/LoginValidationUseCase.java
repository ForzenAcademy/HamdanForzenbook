package com.hamdan.forzenbook.java.login.core.domain.usecase;

import java.io.IOException;

public interface LoginValidationUseCase {
    public void invoke(String email) throws IOException;
}
