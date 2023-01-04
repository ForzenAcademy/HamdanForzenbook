package com.hamdan.forzenbook.di.login

import com.hamdan.forzenbook.data.network.ForgotPasswordService
import com.hamdan.forzenbook.data.repository.ForgotPasswordRepository
import com.hamdan.forzenbook.data.repository.mocks.MockForgotPasswordRepositoryImplSucceeds
import com.hamdan.forzenbook.domain.usecase.login.ForgotPasswordResetUseCase
import com.hamdan.forzenbook.domain.usecase.login.ForgotPasswordResetUseCaseImpl
import com.hamdan.forzenbook.domain.usecase.mocks.MockForgotPasswordResetUseCaseFails
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@InstallIn(ViewModelComponent::class)
@Module
object ForgotPasswordModule {

    @Provides
    @Named(REPO_NAME)
    fun providesForgotPasswordRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOGIN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesForgotPasswordService(@Named(REPO_NAME) retrofit: Retrofit): ForgotPasswordService {
        //return MockForgotPasswordServiceError()
        return retrofit.create(ForgotPasswordService::class.java)
    }

    @Provides
    fun providesForgotPasswordRepository(
        passwordService: ForgotPasswordService
    ): ForgotPasswordRepository {
        return MockForgotPasswordRepositoryImplSucceeds(passwordService)
        //return ForgotPasswordRepositoryImpl(passwordService)
    }

    @Provides
    fun providesForgotPasswordResetUseCase(repository: ForgotPasswordRepository): ForgotPasswordResetUseCase {
        //return MockForgotPasswordResetUseCaseFails()
        return ForgotPasswordResetUseCaseImpl(repository)
    }

    private const val LOGIN_BASE_URL = "https://forzen.dev/"
    private const val REPO_NAME = "forgot_password"
}