package com.hamdan.forzenbook.profile.core.di

import android.content.Context
import androidx.room.Room
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.data.daos.FeedDao
import com.hamdan.forzenbook.data.daos.UserDao
import com.hamdan.forzenbook.data.databases.FeedDatabase
import com.hamdan.forzenbook.data.databases.UserDatabase
import com.hamdan.forzenbook.profile.core.data.network.ProfileService
import com.hamdan.forzenbook.profile.core.data.repository.ProfileRepository
import com.hamdan.forzenbook.profile.core.data.repository.ProfileRepositoryImpl
import com.hamdan.forzenbook.profile.core.domain.GetNewerPostsUseCase
import com.hamdan.forzenbook.profile.core.domain.GetNewerPostsUseCaseImpl
import com.hamdan.forzenbook.profile.core.domain.GetOlderPostsUseCase
import com.hamdan.forzenbook.profile.core.domain.GetOlderPostsUseCaseImpl
import com.hamdan.forzenbook.profile.core.domain.GetPersonalProfileUseCase
import com.hamdan.forzenbook.profile.core.domain.GetPersonalProfileUseCaseImpl
import com.hamdan.forzenbook.profile.core.domain.GetProfileByUserUseCase
import com.hamdan.forzenbook.profile.core.domain.GetProfileByUserUseCaseImpl
import com.hamdan.forzenbook.profile.core.domain.SendAboutUpdateUseCase
import com.hamdan.forzenbook.profile.core.domain.SendAboutUpdateUseCaseImpl
import com.hamdan.forzenbook.profile.core.domain.SendIconUpdateUseCase
import com.hamdan.forzenbook.profile.core.domain.SendIconUpdateUseCaseImpl
import com.hamdan.forzenbook.profile.core.domain.mocks.MockGetNewerPostsUseCase
import com.hamdan.forzenbook.profile.core.domain.mocks.MockGetOlderPostsUseCase
import com.hamdan.forzenbook.profile.core.domain.mocks.MockGetPersonalProfileUseCase
import com.hamdan.forzenbook.profile.core.domain.mocks.MockSendAboutUpdateUseCase
import com.hamdan.forzenbook.profile.core.domain.mocks.MockSendIconUpdateUseCase
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
object ProfileModule {
    @Provides
    @Named(MODULE_NAME)
    fun providesPostRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GlobalConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesProfileService(@Named(MODULE_NAME) retrofit: Retrofit): ProfileService {
        return retrofit.create(ProfileService::class.java)
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
    fun providesProfileRepository(
        @Named(MODULE_NAME) feedDao: FeedDao,
        @Named(MODULE_NAME) userDao: UserDao,
        service: ProfileService,
        @ApplicationContext context: Context
    ): ProfileRepository {
        return ProfileRepositoryImpl(feedDao, userDao, service, context)
    }

    // Todo update names of some of the provides

    @Provides
    fun providesGetBackwardPostsUseCase(
        repository: ProfileRepository
    ): GetNewerPostsUseCase {
        return MockGetNewerPostsUseCase()
//        return GetNewerPostsUseCaseImpl(repository)
    }

    @Provides
    fun providesGetForwardPostsUseCase(
        repository: ProfileRepository
    ): GetOlderPostsUseCase {
        return MockGetOlderPostsUseCase()
//        return GetOlderPostsUseCaseImpl(repository)
    }

    @Provides
    fun providesGetProfileByUserUseCase(
        repository: ProfileRepository
    ): GetProfileByUserUseCase {
        return GetProfileByUserUseCaseImpl(repository)
    }

    @Provides
    fun providesGetPersonalProfileUseCase(
        repository: ProfileRepository
    ): GetPersonalProfileUseCase {
        return MockGetPersonalProfileUseCase()
        // return GetPersonalProfileUseCaseImpl(repository)
    }

    @Provides
    fun providesSendAboutUpdateUseCase(
        repository: ProfileRepository
    ): SendAboutUpdateUseCase {
        return MockSendAboutUpdateUseCase()
        // return SendAboutUpdateUseCaseImpl(repository)
    }

    @Provides
    fun providesSendIconUpdateUseCase(
        repository: ProfileRepository
    ): SendIconUpdateUseCase {
        return MockSendIconUpdateUseCase()
        // return SendIconUpdateUseCaseImpl(repository)
    }

    private const val MODULE_NAME = "profile"
}
