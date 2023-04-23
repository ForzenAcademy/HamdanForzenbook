package com.hamdan.forzenbook.mainpage.core.di

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.hamdan.forzenbook.core.GlobalConstants.LOGIN_BASE_URL
import com.hamdan.forzenbook.mainpage.core.data.database.FeedDao
import com.hamdan.forzenbook.mainpage.core.data.database.FeedDatabase
import com.hamdan.forzenbook.mainpage.core.data.database.UserDao
import com.hamdan.forzenbook.mainpage.core.data.database.UserDatabase
import com.hamdan.forzenbook.mainpage.core.data.network.FeedService
import com.hamdan.forzenbook.mainpage.core.data.repository.FeedRepository
import com.hamdan.forzenbook.mainpage.core.data.repository.FeedRepositoryImpl
import com.hamdan.forzenbook.mainpage.core.domain.GetPostsUseCase
import com.hamdan.forzenbook.mainpage.core.domain.GetPostsUseCaseImpl
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
object FeedModule {

    @Provides
    @Named(MODULE_NAME)
    fun providesFeedRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(LOGIN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun providesFeedService(@Named(MODULE_NAME) retrofit: Retrofit): FeedService {
        return retrofit.create(FeedService::class.java)
    }

    @Provides
    fun providesFeedRoomDataBase(@ApplicationContext context: Context): FeedDatabase {
        return Room.databaseBuilder(context, FeedDatabase::class.java, FeedDatabase.NAME)
            .build()
    }

    @Provides
    fun providesUserRoomDataBase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(context, UserDatabase::class.java, UserDatabase.NAME)
            .build()
    }

    @Provides
    fun providesFeedDao(db: FeedDatabase): FeedDao {
        return db.feedDao()
    }

    @Provides
    fun providesUserDao(db: UserDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    fun providesFeedRepository(
        feedDao: FeedDao,
        userDao: UserDao,
        service: FeedService
    ): FeedRepository {
        return FeedRepositoryImpl(service, feedDao, userDao)
    }

    @Provides
    fun providesGetPostUseCase(repository: FeedRepository): GetPostsUseCase {
        return GetPostsUseCaseImpl(repository)
    }

    private const val MODULE_NAME = "feed"
}
