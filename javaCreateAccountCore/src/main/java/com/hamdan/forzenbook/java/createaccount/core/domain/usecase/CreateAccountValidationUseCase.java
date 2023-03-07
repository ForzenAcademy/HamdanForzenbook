package com.hamdan.forzenbook.java.createaccount.core.domain.usecase;

import androidx.annotation.NonNull;

public interface CreateAccountValidationUseCase {
    @NonNull
    public CreateAccountEntrys invoke(CreateAccountEntrys state);
}
