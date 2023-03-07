package com.hamdan.forzenbook.java.createaccount.core.data.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.hamdan.forzenbook.java.createaccount.core.data.network.CreateAccountResponse;
import com.hamdan.forzenbook.java.createaccount.core.data.network.CreateAccountService;

import java.sql.Date;

import retrofit2.Response;

public class CreateAccountRepositoryImpl implements CreateAccountRepository {

    final private int USER_EXISTS = 409;
    final private int INPUT_ERROR = 400;
    final private CreateAccountService service;


    public CreateAccountRepositoryImpl(CreateAccountService service) {
        this.service = service;
    }

    @Override
    public void createUser(String firstName, String lastName, String birthDay, String email, String location) throws Exception {
        Response<CreateAccountResponse> response = service.createUser(
                email, Date.valueOf(birthDay), firstName, lastName, location
        );
        if (!response.isSuccessful()) {
            if (response.code() == USER_EXISTS) throw (new AccountException("User Already Exists"));
            else if (response.code() == INPUT_ERROR) {
                if (response.errorBody() != null) {
                    CreateAccountResponse msg = new Gson().fromJson(response.errorBody().charStream(), CreateAccountResponse.class);
                    Log.e("CreateAccount", msg.getReason());
                }
                throw (new InputException("Input Exception"));
            } else throw (new Exception("Unknown Error"));
        }
    }
}
