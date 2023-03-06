package com.hamdan.forzenbook.java.login.core.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hamdan.forzenbook.java.login.core.data.database.LoginDao;
import com.hamdan.forzenbook.java.login.core.data.network.LoginResponse;
import com.hamdan.forzenbook.java.login.core.data.network.LoginService;

import retrofit2.Response;

public class LoginRepositoryImpl implements LoginRepository {
    final private LoginDao dao;
    final private LoginService service;

    public LoginRepositoryImpl(LoginDao dao, LoginService service) {
        this.dao = dao;
        this.service = service;
    }

    @Override
    public void requestValidation(String email) {
        service.requestValidation(email);
    }

    @Override
    @NonNull
    public User getToken(@Nullable String email, @Nullable String code) throws NullTokenException, FailTokenRetrievalException {
        if (email == null && code == null) return getTokenFromDatabase();
        else return getTokenFromNetwork(email, code);
    }


    @NonNull
    private User getTokenFromDatabase() {
        if (dao.getToken() == null)
            return User.NOT_LOGGED_IN;
        else return User.LOGGED_IN;
    }

    @NonNull
    private User getTokenFromNetwork(String email, String code) throws FailTokenRetrievalException, NullTokenException {
        Response<LoginResponse> response = service.getToken(email, code);
        if (response.isSuccessful()) {
            if (response.body() == null) {
                throw new FailTokenRetrievalException("Failed to get Token");
            } else {
                LoginResponse body = response.body();
                if (body.getToken() == null) {
                    throw new NullTokenException("null token");
                } else return User.LOGGED_IN;
            }
        } else throw new FailTokenRetrievalException("Failed to get Token");
    }
}
