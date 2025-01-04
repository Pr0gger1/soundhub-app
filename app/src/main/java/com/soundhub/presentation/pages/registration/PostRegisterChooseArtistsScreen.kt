package com.soundhub.presentation.pages.registration

import android.util.Log
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.domain.states.ArtistUiState
import com.soundhub.presentation.shared.layouts.music_preferences.ChooseArtistsScreen
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
fun PostRegisterChooseArtistsScreen(
	registrationViewModel: RegistrationViewModel,
	uiStateDispatcher: UiStateDispatcher
) {
	val artistUiState: ArtistUiState by registrationViewModel.artistUiState.collectAsState()
	val lazyGridState = rememberLazyGridState()

	LaunchedEffect(key1 = true) {
		registrationViewModel.loadArtists()
	}

	LaunchedEffect(key1 = artistUiState) {
		Log.d("PostRegisterChooseArtistsScreen", "ui state: $artistUiState")
	}

	ChooseArtistsScreen(
		artists = artistUiState.artists,
		chosenArtists = artistUiState.chosenArtists,
		isLoading = artistUiState.status.isLoading(),
		onItemPlateClick = registrationViewModel::onArtistItemClick,
		onNextButtonClick = registrationViewModel::onNextButtonClick,
		onSearchFieldChange = registrationViewModel::onSearchFieldChange,
		uiStateDispatcher = uiStateDispatcher,
		lazyGridState = lazyGridState
	)
}