package com.example.forzenbook.di.login

import android.content.Context
import androidx.room.Room
import com.example.forzenbook.data.database.LoginDao
import com.example.forzenbook.data.database.LoginDatabase
import com.example.forzenbook.data.network.LoginService
import com.example.forzenbook.data.repository.LoginRepository
import com.example.forzenbook.data.repository.mocks.MockGetsToken
import com.example.forzenbook.domain.usecase.login.LoginGetTokenUseCase
import com.example.forzenbook.domain.usecase.mocks.MockLoginGetTokenUseCaseSuccess
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
    @Named(REPO_NAME)
    fun providesLoginRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOGIN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesLoginService(@Named(REPO_NAME) retrofit: Retrofit): LoginService {
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
        return MockGetsToken(loginDao, loginService)
        //return LoginRepositoryImpl(loginDao, loginService)
    }

    @Provides
    fun providesLoginGetTokenUseCase(loginRepository: LoginRepository): LoginGetTokenUseCase {
        return MockLoginGetTokenUseCaseSuccess(loginRepository)
    }

    private const val LOGIN_BASE_URL = "https://forzen.dev/"
    private const val REPO_NAME = "login"
}