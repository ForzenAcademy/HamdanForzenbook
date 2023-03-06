package com.hamdan.forzenbook.java.createaccount.core.di;

import static com.hamdan.forzenbook.java.core.GlobalConstants.LOGIN_BASE_URL;

import androidx.annotation.NonNull;

import com.hamdan.forzenbook.java.createaccount.core.data.network.CreateAccountService;
import com.hamdan.forzenbook.java.createaccount.core.data.repository.CreateAccountRepository;
import com.hamdan.forzenbook.java.createaccount.core.data.repository.CreateAccountRepositoryImpl;
import com.hamdan.forzenbook.java.createaccount.core.domain.usecase.CreateAccountUseCase;
import com.hamdan.forzenbook.java.createaccount.core.domain.usecase.CreateAccountUseCaseImpl;
import com.hamdan.forzenbook.java.createaccount.core.domain.usecase.CreateAccountValidationUseCase;
import com.hamdan.forzenbook.java.createaccount.core.domain.usecase.CreateAccountValidationUseCaseImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@InstallIn(ViewModelComponent.class)
@Module
public class JavaCreateAccountModule {

    final private String RETROFIT_NAME = "java_create_account_retrofit";

    @Provides
    @Named(RETROFIT_NAME)
    @NonNull
    public Retrofit providesCreateAccountRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(LOGIN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @NonNull
    public CreateAccountService providesCreateAccountServices(@Named(RETROFIT_NAME) Retrofit retrofit) {
        return retrofit.create(CreateAccountService.class);
    }

    @Provides
    @NonNull
    public CreateAccountRepository providesCreateAccountRepository(CreateAccountService accountRequestService) {
        return new CreateAccountRepositoryImpl(accountRequestService);
    }

    @Provides
    @NonNull
    public CreateAccountUseCase providesCreateAccountRequestUseCase(CreateAccountRepository repository) {
        return new CreateAccountUseCaseImpl(repository);
    }

    @Provides
    @NonNull
    public CreateAccountValidationUseCase providesCreateAccountStringValidationUseCase() {
        return new CreateAccountValidationUseCaseImpl();
    }
}
