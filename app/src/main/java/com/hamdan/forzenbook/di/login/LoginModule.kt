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
import com.hamdan.forzenbook.domain.usecase.login.LoginGetTokenUseCase
import com.hamdan.forzenbook.domain.usecase.login.LoginGetTokenUseCaseImpl
import com.hamdan.forzenbook.domain.usecase.login.LoginRequestValidationUseCase
import com.hamdan.forzenbook.domain.usecase.login.LoginRequestValidationUseCaseImpl
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
    fun providesLoginGetTokenUseCase(loginRepository: LoginRepository): LoginGetTokenUseCase {
        // return MockLoginGetTokenUseCaseFails()
        // return MockLoginGetTokenUseCaseSuccess(loginRepository)
        return LoginGetTokenUseCaseImpl(loginRepository)
    }

    @Provides
    fun providesLoginRequestValidationUseCase(loginRepository: LoginRepository): LoginRequestValidationUseCase {
        return LoginRequestValidationUseCaseImpl(loginRepository)
    }

    private const val MODULE_NAME = "login"
}
