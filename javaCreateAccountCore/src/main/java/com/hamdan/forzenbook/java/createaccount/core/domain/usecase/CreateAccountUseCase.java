package com.hamdan.forzenbook.java.createaccount.core.domain.usecase;

import androidx.annotation.NonNull;

public interface CreateAccountUseCase {
    @NonNull
    public CreateAccountResult invoke(
            String firstName,
            String lastName,
            String birthDay,
            String email,
            String location
    ) throws Exception;
}
