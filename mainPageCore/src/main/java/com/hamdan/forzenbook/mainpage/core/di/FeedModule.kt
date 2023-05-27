package com.hamdan.forzenbook.mainpage.core.di

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.hamdan.forzenbook.core.GlobalConstants.LOGIN_BASE_URL
import com.hamdan.forzenbook.data.daos.FeedDao
import com.hamdan.forzenbook.data.daos.UserDao
import com.hamdan.forzenbook.data.databases.FeedDatabase
import com.hamdan.forzenbook.data.databases.UserDatabase
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
    @Named(MODULE_NAME)
    fun providesFeedRoomDataBase(@ApplicationContext context: Context): FeedDatabase {
        return Room.databaseBuilder(context, FeedDatabase::class.java, FeedDatabase.NAME)
            .build()
    }

    @Provides
    @Named(MODULE_NAME)
    fun providesUserRoomDataBase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(context, UserDatabase::class.java, UserDatabase.NAME)
            .build()
    }

    @Provides
    @Named(MODULE_NAME)
    fun providesFeedDao(@Named(MODULE_NAME) db: FeedDatabase): FeedDao {
        return db.feedDao()
    }

    @Provides
    @Named(MODULE_NAME)
    fun providesUserDao(@Named(MODULE_NAME) db: UserDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    fun providesFeedRepository(
        @Named(MODULE_NAME) feedDao: FeedDao,
        @Named(MODULE_NAME) userDao: UserDao,
        service: FeedService,
        @ApplicationContext context: Context,
    ): FeedRepository {
        return FeedRepositoryImpl(service, feedDao, userDao, context)
    }

    @Provides
    fun providesGetPostUseCase(repository: FeedRepository): GetPostsUseCase {
        return GetPostsUseCaseImpl(repository)
    }

    private const val MODULE_NAME = "feed"
}
