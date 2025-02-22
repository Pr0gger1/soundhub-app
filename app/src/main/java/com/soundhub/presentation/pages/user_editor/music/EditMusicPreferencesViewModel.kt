package com.soundhub.presentation.pages.user_editor.music

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.domain.events.UiEvent
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Genre
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.ArtistRepository
import com.soundhub.domain.states.UiState
import com.soundhub.domain.usecases.music.LoadGenresUseCase
import com.soundhub.domain.usecases.user.UpdateUserUseCase
import com.soundhub.presentation.viewmodels.BaseMusicPreferencesViewModel
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.lib.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditMusicPreferencesViewModel @Inject constructor(
	private val uiStateDispatcher: UiStateDispatcher,
	private val updateUserUseCase: UpdateUserUseCase,
	private val loadGenresUseCase: LoadGenresUseCase,
	private val userDao: UserDao,
	artistRepository: ArtistRepository,
) : BaseMusicPreferencesViewModel(
	loadGenresUseCase = loadGenresUseCase,
	uiStateDispatcher = uiStateDispatcher,
	artistRepository = artistRepository
) {
	private var searchText = MutableStateFlow<String?>(null)
	private val uiState: Flow<UiState> = uiStateDispatcher.uiState

	init {
		uiStateDispatcher.onSearchValueDebounceChange { text ->
			searchText.update { text }
		}
	}

	override fun onCleared() {
		super.onCleared()
		searchText.update { null }

		Log.d("EditMusicPreferencesViewModel", "viewmodel was cleared")
	}

	override fun loadGenres(): Job = viewModelScope.launch {
		val user: User? = userDao.getCurrentUser()
		val (_, genres: List<Genre>, chosenGenres: List<Genre>) = _genreUiState.value
		val favoriteGenres: List<Genre> = user?.favoriteGenres.orEmpty()

		if (chosenGenres.isEmpty())
			_genreUiState.update { it.copy(chosenGenres = favoriteGenres) }

		if (genres.isEmpty())
			loadGenresUseCase(genreUiState = _genreUiState)
		Log.d("EditMusicPreferencesViewModel", "loadGenres: ${genreUiState.value}")
	}

	override fun onNextButtonClick() = viewModelScope.launch(Dispatchers.Main) {
		val state: UiState? = uiState.firstOrNull()
		when (state?.currentRoute) {
			Route.EditFavoriteGenres.route -> onChooseGenresNextButtonClick()
			Route.EditFavoriteArtists.route -> onChooseArtistsNextButtonClick()
			else -> Unit
		}
	}

	override fun onChooseGenresNextButtonClick() = viewModelScope.launch(Dispatchers.Main) {
		if (_genreUiState.value.chosenGenres.isEmpty()) {
			val toastMessage =
				UiEvent.ShowToast(UiText.StringResource(R.string.choose_genres_warning))

			uiStateDispatcher.sendUiEvent(toastMessage)
			return@launch
		}

		uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.EditFavoriteArtists))
	}

	override fun onChooseArtistsNextButtonClick() = viewModelScope.launch(Dispatchers.IO) {
		var toastMessage = UiText.StringResource(R.string.toast_update_music_preferences_successful)

		if (_artistUiState.value.chosenArtists.isEmpty()) {
			toastMessage = UiText.StringResource(R.string.choose_artist_warning)
			uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastMessage))
			return@launch
		}

		val user: User? = getUpdatedUser()

		updateUserUseCase(user).onSuccess {
			withContext(Dispatchers.Main) {
				user?.let {
					val route: Route = Route.Profile.getRouteWithNavArg(user.id.toString())
					userDao.saveOrReplaceUser(user)
					with(uiStateDispatcher) {
						setAuthorizedUser(user)
						sendUiEvent(UiEvent.ShowToast(toastMessage))
						sendUiEvent(UiEvent.Navigate(route))
					}
				}
			}
		}
			.onFailure {
				toastMessage = UiText.StringResource(R.string.toast_update_music_preferences_failed)
				uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastMessage))
			}
	}

	private suspend fun getUpdatedUser(): User? {
		val authorizedUser: User? = userDao.getCurrentUser()
		val artistUnion: List<Artist> = authorizedUser?.favoriteArtists
			.orEmpty()
			.union(artistUiState.value.chosenArtists)
			.toList()

		val genreUnion: List<Genre> = authorizedUser?.favoriteGenres
			.orEmpty()
			.union(genreUiState.value.chosenGenres)
			.toList()

		val user: User? = authorizedUser?.copy(
			favoriteArtists = artistUnion,
			favoriteGenres = genreUnion,
			favoriteArtistsIds = artistUnion.map { it.id }
		)

		return user
	}
}
