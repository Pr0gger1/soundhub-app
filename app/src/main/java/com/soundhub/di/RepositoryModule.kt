package com.soundhub.di

import android.content.Context
import com.google.gson.Gson
import com.soundhub.data.api.AuthService
import com.soundhub.data.api.ChatService
import com.soundhub.data.api.CountryService
import com.soundhub.data.api.FileService
import com.soundhub.data.api.GenreService
import com.soundhub.data.api.InviteService
import com.soundhub.data.api.MessageService
import com.soundhub.data.api.MusicService
import com.soundhub.data.api.PostService
import com.soundhub.data.api.UserService
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.ChatRepository
import com.soundhub.data.repository.CountryRepository
import com.soundhub.data.repository.FileRepository
import com.soundhub.data.repository.InviteRepository
import com.soundhub.data.repository.MessageRepository
import com.soundhub.data.repository.MusicRepository
import com.soundhub.data.repository.PostRepository
import com.soundhub.data.repository.UserRepository
import com.soundhub.data.repository.implementations.AuthRepositoryImpl
import com.soundhub.data.repository.implementations.ChatRepositoryImpl
import com.soundhub.data.repository.implementations.CountryRepositoryImpl
import com.soundhub.data.repository.implementations.FileRepositoryImpl
import com.soundhub.data.repository.implementations.InviteRepositoryImpl
import com.soundhub.data.repository.implementations.MessageRepositoryImpl
import com.soundhub.data.repository.implementations.MusicRepositoryImpl
import com.soundhub.data.repository.implementations.PostRepositoryImpl
import com.soundhub.data.repository.implementations.UserRepositoryImpl
import com.soundhub.domain.usecases.user.LoadAllUserDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesInviteRepository(
        inviteService: InviteService,
        loadAllUserDataUseCase: LoadAllUserDataUseCase
    ): InviteRepository = InviteRepositoryImpl(inviteService, loadAllUserDataUseCase)

    @Provides
    @Singleton
    fun providesAuthRepository(
        @ApplicationContext
        context: Context,
        authService: AuthService,
        userRepository: UserRepository,
        gson: Gson
    ): AuthRepository = AuthRepositoryImpl(
        authService = authService,
        userRepository = userRepository,
        context = context,
        gson = gson
    )

    @Provides
    @Singleton
    fun providesUserRepository(
        @ApplicationContext
        context: Context,
        userService: UserService,
        loadAllUserDataUseCase: LoadAllUserDataUseCase,
        gson: Gson
    ): UserRepository = UserRepositoryImpl(
        userService = userService,
        loadAllUserDataUseCase = loadAllUserDataUseCase,
        context = context,
        gson = gson
    )

    @Provides
    @Singleton
    fun providesPostRepository(
        @ApplicationContext
        context: Context,
        postService: PostService,
        loadAllUserDataUseCase: LoadAllUserDataUseCase,
        gson: Gson
    ): PostRepository = PostRepositoryImpl(
        postService = postService,
        loadAllUserDataUseCase = loadAllUserDataUseCase,
        context = context,
        gson = gson
    )

    @Provides
    @Singleton
    fun providesCountryRepository(
        countryService: CountryService
    ): CountryRepository = CountryRepositoryImpl(countryService)

    @Provides
    @Singleton
    fun providesChatRepository(
        chatService: ChatService
    ): ChatRepository = ChatRepositoryImpl(chatService)

    @Provides
    @Singleton
    fun providesFileRepository(
        @ApplicationContext
        context: Context,
        fileService: FileService
    ): FileRepository = FileRepositoryImpl(fileService, context)

    @Provides
    @Singleton
    fun providesMusicRepository(
        @ApplicationContext
        context: Context,
        musicService: MusicService,
        genreService: GenreService
    ): MusicRepository = MusicRepositoryImpl(musicService, genreService, context)

    @Provides
    @Singleton
    fun providesMessageRepository(
        messageService: MessageService,
        loadAllUserDataUseCase: LoadAllUserDataUseCase
    ): MessageRepository = MessageRepositoryImpl(messageService, loadAllUserDataUseCase)
}