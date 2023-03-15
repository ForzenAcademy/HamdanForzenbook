package com.hamdan.forzenbook.java.login.core.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

public interface LoginRepository {
    public void requestValidation(String email) throws IOException;

    @NonNull
    public User getToken(@Nullable String email, @Nullable String code) throws NullTokenException, FailTokenRetrievalException, IOException;
}
