package com.hamdan.forzenbook.di.login

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.hamdan.forzenbook.data.database.LoginDao
import com.hamdan.forzenbook.data.database.LoginDatabase
import com.hamdan.forzenbook.data.network.LoginService
import com.hamdan.forzenbook.data.repository.LoginRepository
import com.hamdan.forzenbook.data.repository.LoginRepositoryImpl
import com.hamdan.forzenbook.di.login.ModuleSharedValues.LOGIN_BASE_URL
import com.hamdan.forzenbook.domain.usecase.login.LoginUseCaseGetCredentialsFromNetwork
import com.hamdan.forzenbook.domain.usecase.login.LoginUseCaseGetCredentialsFromNetworkImpl
import com.hamdan.forzenbook.domain.usecase.login.LoginUseCaseGetStoredCredentials
import com.hamdan.forzenbook.domain.usecase.login.LoginUseCaseGetStoredCredentialsImpl
import com.hamdan.forzenbook.domain.usecase.login.LoginUseCaseValidation
import com.hamdan.forzenbook.domain.usecase.login.LoginUseCaseValidationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@InstallIn(ViewModelComponent::class)
@Module
object LoginModule {

    @Provides
    @Named(MODULE_NAME)
    fun providesLoginRetrofit(): Retrofit {
        // TODO remove this GsonBuilder when backend updates database to avoid sending malformed JSON
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(LOGIN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun providesLoginService(@Named(MODULE_NAME) retrofit: Retrofit): LoginService {
        // return MockLoginServiceNullToken()
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    fun providesRoomDataBase(@ApplicationContext context: Context): LoginDatabase {
        return Room.databaseBuilder(context, LoginDatabase::class.java, LoginDatabase.NAME)
            .build()
    }

    @Provides
    fun providesLoginDao(db: LoginDatabase): LoginDao {
        return db.loginDao()
    }

    @Provides
    fun providesLoginRepository(
        loginDao: LoginDao,
        loginService: LoginService
    ): LoginRepository {
        // return MockGetsToken(loginDao, loginService)
        return LoginRepositoryImpl(loginDao, loginService)
    }

    @Provides
    fun providesLoginGetTokenFromNetworkUseCase(loginRepository: LoginRepository): LoginUseCaseGetCredentialsFromNetwork {
        // return MockLoginGetTokenUseCaseFails()
        // return MockLoginGetTokenUseCaseSuccess(loginRepository)
        return LoginUseCaseGetCredentialsFromNetworkImpl(loginRepository)
    }

    @Provides
    fun providesLoginGetTokenFromDatabaseUseCaseImpl(loginRepository: LoginRepository): LoginUseCaseGetStoredCredentials {
        return LoginUseCaseGetStoredCredentialsImpl(loginRepository)
    }

    @Provides
    fun providesLoginRequestValidationUseCase(loginRepository: LoginRepository): LoginUseCaseValidation {
        return LoginUseCaseValidationImpl(loginRepository)
    }

    private const val MODULE_NAME = "login"
}
