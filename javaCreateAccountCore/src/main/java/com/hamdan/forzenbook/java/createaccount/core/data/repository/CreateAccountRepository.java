package com.hamdan.forzenbook.java.createaccount.core.data.repository;

public interface CreateAccountRepository {
    public void createUser(
            String firstName,
            String lastName,
            String birthDay,
            String email,
            String location
    ) throws Exception;
}
