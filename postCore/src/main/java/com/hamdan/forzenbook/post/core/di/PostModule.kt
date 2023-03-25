package com.hamdan.forzenbook.post.core.di

import com.hamdan.forzenbook.core.GlobalConstants.LOGIN_BASE_URL
import com.hamdan.forzenbook.post.core.data.network.PostService
import com.hamdan.forzenbook.post.core.data.repository.PostRepository
import com.hamdan.forzenbook.post.core.data.repository.PostRepositoryImpl
import com.hamdan.forzenbook.post.core.domain.SendImagePostUseCase
import com.hamdan.forzenbook.post.core.domain.SendImagePostUseCaseImpl
import com.hamdan.forzenbook.post.core.domain.SendTextPostUseCase
import com.hamdan.forzenbook.post.core.domain.SendTextPostUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@InstallIn(ViewModelComponent::class)
@Module
object PostModule {
    // Todo fill out with any items needed to be injected by dagger for PostViewModel
    @Provides
    @Named(MODULE_NAME)
    fun providesPostRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOGIN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesPostService(@Named(MODULE_NAME) retrofit: Retrofit): PostService {
        return retrofit.create(PostService::class.java)
    }

    @Provides
    fun providesPostRepository(service: PostService): PostRepository {
        return PostRepositoryImpl(service)
    }

    @Provides
    fun providesSendImagePostUseCase(repository: PostRepository): SendImagePostUseCase {
        return SendImagePostUseCaseImpl(repository)
    }

    @Provides
    fun providesSendTextPostUseCase(repository: PostRepository): SendTextPostUseCase {
        return SendTextPostUseCaseImpl(repository)
    }

    private const val MODULE_NAME = "post"
}
