package com.soundhub.di

import com.soundhub.domain.repository.ChatRepository
import com.soundhub.domain.repository.FileRepository
import com.soundhub.domain.repository.MusicRepository
import com.soundhub.domain.repository.PostRepository
import com.soundhub.domain.repository.UserRepository
import com.soundhub.domain.usecases.chat.GetAllChatsByUserUseCase
import com.soundhub.domain.usecases.chat.GetOrCreateChatByUserUseCase
import com.soundhub.domain.usecases.file.GetImageUseCase
import com.soundhub.domain.usecases.music.LoadArtistsUseCase
import com.soundhub.domain.usecases.music.LoadGenresUseCase
import com.soundhub.domain.usecases.music.SearchArtistsUseCase
import com.soundhub.domain.usecases.post.DeletePostByIdUseCase
import com.soundhub.domain.usecases.post.GetPostsByUserUseCase
import com.soundhub.domain.usecases.post.TogglePostLikeAndUpdateListUseCase
import com.soundhub.domain.usecases.user.GetUserByIdUseCase
import com.soundhub.domain.usecases.user.LoadAllUserDataUseCase
import com.soundhub.domain.usecases.user.UpdateUserUseCase
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
	@Provides
	@Singleton
	fun providesUpdateUserUseCase(
		userRepository: UserRepository
	): UpdateUserUseCase = UpdateUserUseCase(userRepository)

	@Provides
	@Singleton
	fun providesGetImageUseCase(fileRepository: FileRepository): GetImageUseCase =
		GetImageUseCase(fileRepository)

	@Provides
	@Singleton
	fun providesLoadGenresUseCase(
		musicRepository: MusicRepository,
		uiStateDispatcher: UiStateDispatcher
	): LoadGenresUseCase = LoadGenresUseCase(musicRepository, uiStateDispatcher)

	@Provides
	@Singleton
	fun providesLoadArtistsUseCase(musicRepository: MusicRepository): LoadArtistsUseCase =
		LoadArtistsUseCase(musicRepository)

	@Provides
	@Singleton
	fun providesGetAllChatsByUserUseCase(chatRepository: ChatRepository): GetAllChatsByUserUseCase =
		GetAllChatsByUserUseCase(chatRepository)

	@Provides
	@Singleton
	fun providesGetOrCreateChatUseCase(chatRepository: ChatRepository): GetOrCreateChatByUserUseCase =
		GetOrCreateChatByUserUseCase(chatRepository)

	@Provides
	@Singleton
	fun providesSearchArtistsUseCase(musicRepository: MusicRepository): SearchArtistsUseCase =
		SearchArtistsUseCase(musicRepository)

	@Provides
	@Singleton
	fun providesGetUserByIdUseCase(
		userRepository: UserRepository,
	): GetUserByIdUseCase = GetUserByIdUseCase(userRepository)

	@Provides
	@Singleton
	fun providesLoadAllUserDataUseCase(musicRepository: MusicRepository): LoadAllUserDataUseCase =
		LoadAllUserDataUseCase(
			musicRepository = musicRepository,
		)

	@Provides
	@Singleton
	fun providesGetPostsByUserUseCase(
		postRepository: PostRepository,
	): GetPostsByUserUseCase = GetPostsByUserUseCase(postRepository)

	@Provides
	@Singleton
	fun providesDeletePostByIdUseCase(
		postRepository: PostRepository,
	): DeletePostByIdUseCase = DeletePostByIdUseCase(postRepository)

	@Provides
	@Singleton
	fun providesTogglePostLikeAndUpdateListUseCase(
		postRepository: PostRepository
	): TogglePostLikeAndUpdateListUseCase =
		TogglePostLikeAndUpdateListUseCase(postRepository)
}