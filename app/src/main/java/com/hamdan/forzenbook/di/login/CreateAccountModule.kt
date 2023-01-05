package com.hamdan.forzenbook.di.login

import com.hamdan.forzenbook.data.network.CreateAccountService
import com.hamdan.forzenbook.data.repository.CreateAccountRepository
import com.hamdan.forzenbook.data.repository.mocks.MockCreateAccountRepositoryImplSucceeds
import com.hamdan.forzenbook.domain.usecase.login.CreateAccountRequestUseCase
import com.hamdan.forzenbook.domain.usecase.login.CreateAccountRequestUseCaseImpl
import com.hamdan.forzenbook.domain.usecase.mocks.MockCreateAccountRequestUseCaseFails
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@InstallIn(ViewModelComponent::class)
@Module
object CreateAccountModule {

    @Provides
    @Named(REPO_NAME)
    fun providesCreateAccountRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOGIN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesCreateAccountService(@Named(REPO_NAME) retrofit: Retrofit): CreateAccountService {
        //return MockCreateAccountServiceError()
        return retrofit.create(CreateAccountService::class.java)
    }

    @Provides
    fun providesCreateAccountRepository(
        accountRequestService: CreateAccountService
    ): CreateAccountRepository {

        //return CreateAccountRepositoryImpl(accountRequestService) //can't really use this mock yet, need more info on the api
        return MockCreateAccountRepositoryImplSucceeds(accountRequestService)
    }

    @Provides
    fun providesCreateAccountRequestUseCase(repository: CreateAccountRepository): CreateAccountRequestUseCase {
        //return MockCreateAccountRequestUseCaseFails()
        return CreateAccountRequestUseCaseImpl(repository)
    }

    private const val LOGIN_BASE_URL = "https://forzen.dev/"
    private const val REPO_NAME = "create_account"
}