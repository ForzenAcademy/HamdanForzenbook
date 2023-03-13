package com.hamdan.forzenbook.java.login.core.data.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {
    final static String EMAIL = "email";
    final static String CODE = "code";
    final static String LOGIN_GET = "login";
    final static String LOGIN_POST = "login/";

    @GET(LOGIN_GET)
    public Call<Void> requestValidation(
            @Query(EMAIL) String email
    );

    @FormUrlEncoded
    @POST(LOGIN_POST)
    public Call<LoginResponse> getToken(
            @Field(EMAIL) String email,
            @Field(CODE) String code
    );
}
