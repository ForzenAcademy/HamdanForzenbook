package com.hamdan.forzenbook.java.createaccount.core.domain.usecase;

import androidx.annotation.NonNull;

import com.hamdan.forzenbook.java.createaccount.core.data.repository.AccountException;
import com.hamdan.forzenbook.java.createaccount.core.data.repository.CreateAccountRepository;

public class CreateAccountUseCaseImpl implements CreateAccountUseCase {

    final private CreateAccountRepository repository;

    public CreateAccountUseCaseImpl(CreateAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    @NonNull
    public CreateAccountResult invoke(String firstName, String lastName, String birthDay, String email, String location) {
        try {
            repository.createUser(firstName, lastName, birthDay, email, location);
            return CreateAccountResult.CREATE_SUCCESS;
        } catch (AccountException e) {
            return CreateAccountResult.CREATE_EXISTS;
        } catch (Exception e) {
            return CreateAccountResult.CREATE_FAILURE;
        }
    }
}
