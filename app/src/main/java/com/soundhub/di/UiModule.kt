package com.soundhub.di

import com.soundhub.data.datastore.UserSettingsStore
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UiModule {
	@Provides
	@Singleton
	fun providesUiEventDispatcher(userSettingsStore: UserSettingsStore): UiStateDispatcher {
		return UiStateDispatcher(userSettingsStore)
	}
}