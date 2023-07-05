package com.hamdan.forzenbook.search.core.di

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.data.daos.FeedDao
import com.hamdan.forzenbook.data.daos.UserDao
import com.hamdan.forzenbook.data.databases.FeedDatabase
import com.hamdan.forzenbook.data.databases.UserDatabase
import com.hamdan.forzenbook.search.core.data.network.SearchService
import com.hamdan.forzenbook.search.core.data.repository.SearchRepository
import com.hamdan.forzenbook.search.core.data.repository.SearchRepositoryImpl
import com.hamdan.forzenbook.search.core.domain.GetPostByStringUseCase
import com.hamdan.forzenbook.search.core.domain.GetPostByStringUseCaseImpl
import com.hamdan.forzenbook.search.core.domain.GetPostByUserIdUseCase
import com.hamdan.forzenbook.search.core.domain.GetPostByUserIdUseCaseImpl
import com.hamdan.forzenbook.search.core.domain.SearchForPostByIdUseCase
import com.hamdan.forzenbook.search.core.domain.SearchForPostByIdUseCaseImpl
import com.hamdan.forzenbook.search.core.domain.SearchForPostByStringUseCase
import com.hamdan.forzenbook.search.core.domain.SearchForPostByStringUseCaseImpl
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
object SearchModule {

    @Provides
    @Named(MODULE_NAME)
    fun providesSearchRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(GlobalConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun providesSearchService(@Named(MODULE_NAME) retrofit: Retrofit): SearchService {
        return retrofit.create(SearchService::class.java)
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
    fun providesSearchRepository(
        searchService: SearchService,
        @Named(MODULE_NAME) feedDao: FeedDao,
        @Named(MODULE_NAME) userDao: UserDao,
        @ApplicationContext context: Context,
    ): SearchRepository {
        return SearchRepositoryImpl(feedDao, userDao, searchService, context)
    }

    @Provides
    fun providesSearchGetPostByStringUseCase(repository: SearchRepository): SearchForPostByStringUseCase {
        return SearchForPostByStringUseCaseImpl(repository)
    }

    @Provides
    fun providesSearchGetPostByUserIdUseCase(repository: SearchRepository): SearchForPostByIdUseCase {
        return SearchForPostByIdUseCaseImpl(repository)
    }

    @Provides
    fun providesGetPostByStringUseCase(repository: SearchRepository): GetPostByStringUseCase {
        return GetPostByStringUseCaseImpl(repository)
    }

    @Provides
    fun providesGetPostByUserIdUseCase(repository: SearchRepository): GetPostByUserIdUseCase {
        return GetPostByUserIdUseCaseImpl(repository)
    }

    private const val MODULE_NAME = "search"
}
