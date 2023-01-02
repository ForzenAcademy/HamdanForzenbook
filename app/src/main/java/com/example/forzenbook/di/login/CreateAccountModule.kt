package com.example.forzenbook.di.login

import com.example.forzenbook.data.network.CreateAccountService
import com.example.forzenbook.data.repository.CreateAccountRepository
import com.example.forzenbook.data.repository.mocks.MockCreateAccountRepositoryImplSucceeds
import com.example.forzenbook.domain.usecase.login.CreateAccountRequestUseCase
import com.example.forzenbook.domain.usecase.login.CreateAccountRequestUseCaseImpl
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
        return retrofit.create(CreateAccountService::class.java)
    }

    @Provides
    fun providesCreateAccountRepository(
        accountRequestService: CreateAccountService
    ): CreateAccountRepository {
        return MockCreateAccountRepositoryImplSucceeds(accountRequestService)
    }

    @Provides
    fun providesCreateAccountRequestUseCase(repository: CreateAccountRepository): CreateAccountRequestUseCase {
        return CreateAccountRequestUseCaseImpl(repository)
    }

    private const val LOGIN_BASE_URL = "https://forzen.dev/"
    private const val REPO_NAME = "create_account"
}