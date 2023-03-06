package com.hamdan.forzenbook.java.login.core.di;

import static com.hamdan.forzenbook.java.core.GlobalConstants.LOGIN_BASE_URL;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hamdan.forzenbook.java.login.core.data.database.LoginDao;
import com.hamdan.forzenbook.java.login.core.data.database.LoginDatabase;
import com.hamdan.forzenbook.java.login.core.data.network.LoginService;
import com.hamdan.forzenbook.java.login.core.data.repository.LoginRepository;
import com.hamdan.forzenbook.java.login.core.data.repository.LoginRepositoryImpl;
import com.hamdan.forzenbook.java.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCase;
import com.hamdan.forzenbook.java.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCaseImpl;
import com.hamdan.forzenbook.java.login.core.domain.usecase.LoginGetStoredCredentialsUseCase;
import com.hamdan.forzenbook.java.login.core.domain.usecase.LoginGetStoredCredentialsUseCaseImpl;
import com.hamdan.forzenbook.java.login.core.domain.usecase.LoginStringValidationUseCase;
import com.hamdan.forzenbook.java.login.core.domain.usecase.LoginStringValidationUseCaseImpl;
import com.hamdan.forzenbook.java.login.core.domain.usecase.LoginValidationUseCase;
import com.hamdan.forzenbook.java.login.core.domain.usecase.LoginValidationUseCaseImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@InstallIn(ViewModelComponent.class)
@Module
public class JavaLoginModule {
    final private String RETROFIT_NAME = "java_login_retrofit";

    @Provides
    @Named(RETROFIT_NAME)
    @NonNull
    public Retrofit providesCreateAccountRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(LOGIN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @NonNull
    public LoginService providesLoginService(@Named(RETROFIT_NAME) Retrofit retrofit) {
        return retrofit.create(LoginService.class);
    }

    @Provides
    @NonNull
    public LoginDatabase providesRoomDataBase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, LoginDatabase.class, LoginDatabase.NAME)
                .build();
    }

    @Provides
    @NonNull
    public LoginDao providesLoginDao(LoginDatabase db) {
        return db.loginDao();
    }

    @Provides
    @NonNull
    public LoginRepository providesLoginRepository(LoginDao loginDao, LoginService loginService) {
        return new LoginRepositoryImpl(loginDao, loginService);
    }

    @Provides
    @NonNull
    public LoginGetCredentialsFromNetworkUseCase LoginGetCredentialsFromNetworkUseCase(LoginRepository loginRepository) {
        return new LoginGetCredentialsFromNetworkUseCaseImpl(loginRepository);
    }

    @Provides
    @NonNull
    public LoginGetStoredCredentialsUseCase providesLoginGetTokenFromDatabaseUseCaseImpl(LoginRepository loginRepository) {
        return new LoginGetStoredCredentialsUseCaseImpl(loginRepository);
    }

    @Provides
    @NonNull
    public LoginValidationUseCase providesLoginRequestValidationUseCase(LoginRepository loginRepository) {
        return new LoginValidationUseCaseImpl(loginRepository);
    }

    @Provides
    @NonNull
    public LoginStringValidationUseCase providesLoginStringValidationUseCase() {
        return new LoginStringValidationUseCaseImpl();
    }
}
