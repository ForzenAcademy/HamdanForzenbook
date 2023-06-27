package com.hamdan.forzenbook.post.core.di

import android.content.Context
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
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@InstallIn(ViewModelComponent::class)
@Module
object PostModule {
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
    fun providesPostRepository(
        service: PostService,
        @ApplicationContext context: Context
    ): PostRepository {
        return PostRepositoryImpl(service, context)
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
