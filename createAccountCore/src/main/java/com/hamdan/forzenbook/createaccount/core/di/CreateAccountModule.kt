package com.hamdan.forzenbook.createaccount.core.di

import com.hamdan.forzenbook.core.GlobalConstants.LOGIN_BASE_URL
import com.hamdan.forzenbook.createaccount.core.data.network.CreateAccountService
import com.hamdan.forzenbook.createaccount.core.data.repository.CreateAccountRepository
import com.hamdan.forzenbook.createaccount.core.data.repository.CreateAccountRepositoryImpl
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCaseImpl
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCaseImpl
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
    @Named(MODULE_NAME)
    fun providesCreateAccountRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOGIN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesCreateAccountService(@Named(MODULE_NAME) retrofit: Retrofit): CreateAccountService {
        // return MockCreateAccountServiceError()
        return retrofit.create(CreateAccountService::class.java)
    }

    @Provides
    fun providesCreateAccountRepository(
        accountRequestService: CreateAccountService
    ): CreateAccountRepository {
        return CreateAccountRepositoryImpl(accountRequestService)
        // return MockCreateAccountRepositoryImplSucceeds(accountRequestService)
    }

    @Provides
    fun providesCreateAccountRequestUseCase(repository: CreateAccountRepository): CreateAccountUseCase {
        // return MockCreateAccountRequestUseCaseFails()
        return CreateAccountUseCaseImpl(repository)
        // return MockCreateAccountUseCaseBadData(repository)
    }

    @Provides
    fun providesCreateAccountStringValidationUseCase(): CreateAccountValidationUseCase {
        return CreateAccountValidationUseCaseImpl()
    }

    private const val MODULE_NAME = "create_account"
}
