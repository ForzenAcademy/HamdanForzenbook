package com.hamdan.forzenbook.java.createaccount.core.data.network;

import java.sql.Date;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CreateAccountService {

    final static String EMAIL = "email";
    final static String BIRTH_DATE = "birth_date";
    final static String FIRST_NAME = "first_name";
    final static String LAST_NAME = "last_name";
    final static String LOCATION = "location";
    final static String CREATE_USER = "user/";

    @FormUrlEncoded
    @POST(CREATE_USER)
    public Response<CreateAccountResponse> createUser(
            @Field(EMAIL) String email,
            @Field(BIRTH_DATE) Date birthDate,
            @Field(FIRST_NAME) String firstName,
            @Field(LAST_NAME) String lastName,
            @Field(LOCATION) String location
    );
}
