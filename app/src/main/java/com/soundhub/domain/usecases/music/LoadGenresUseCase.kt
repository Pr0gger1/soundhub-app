package com.soundhub.domain.usecases.music

import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.events.UiEvent
import com.soundhub.domain.repository.GenreRepository
import com.soundhub.domain.states.GenreUiState
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.lib.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LoadGenresUseCase @Inject constructor(
	private val uiStateDispatcher: UiStateDispatcher,
	private val genreRepository: GenreRepository
) {
	suspend operator fun invoke(
		countPerPage: Int = 50,
		genreUiState: MutableStateFlow<GenreUiState>
	) {
		genreRepository.getAllGenres(countPerPage)
			.onSuccess { response ->
				genreUiState.update {
					it.copy(
						status = ApiStatus.SUCCESS,
						genres = response.body.orEmpty()
					)
				}
			}
			.onFailure { error ->
				genreUiState.update { it.copy(status = error.status) }
				error.errorBody.detail?.let { e ->
					uiStateDispatcher.sendUiEvent(
						UiEvent.ShowToast(UiText.DynamicString(e))
					)
				}
			}
	}
}