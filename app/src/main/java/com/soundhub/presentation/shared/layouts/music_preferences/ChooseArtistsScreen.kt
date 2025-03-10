package com.soundhub.presentation.shared.layouts.music_preferences

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.soundhub.R
import com.soundhub.data.datastore.UserSettingsStore
import com.soundhub.domain.model.Artist
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.layout.PostRegisterGridLayout
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import java.util.UUID

@Composable
fun ChooseArtistsScreen(
	pagedArtists: LazyPagingItems<Artist>? = null,
	chosenArtists: List<Artist>,
	isLoading: Boolean = false,
	onItemPlateClick: (isChosen: Boolean, artist: Artist) -> Unit,
	onNextButtonClick: () -> Unit,
	onSearchFieldChange: (value: String) -> Unit,
	uiStateDispatcher: UiStateDispatcher,
	lazyGridState: LazyGridState,
) {
	val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())

	PostRegisterGridLayout<UUID>(
		pagedItems = pagedArtists,
		chosenItems = chosenArtists,
		title = stringResource(id = R.string.screen_title_choose_artists),
		onItemPlateClick = { isChosen, item -> onItemPlateClick(isChosen, item as Artist) },
		onNextButtonClick = onNextButtonClick,
		lazyGridState = lazyGridState,
		isLoading = isLoading,
		topContent = {
			OutlinedTextField(
				value = uiState.searchBarText,
				onValueChange = onSearchFieldChange,
				colors = TextFieldDefaults.colors(
					unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
				),
				singleLine = true,
				trailingIcon = {
					Icon(imageVector = Icons.Rounded.Search, contentDescription = "search bar")
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 5.dp),
			)
		}
	)
}

@Composable
@Preview(
	showSystemUi = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES
			or Configuration.UI_MODE_TYPE_NORMAL
)
private fun ChooseArtistsPreview() {
	val context = LocalContext.current
	ChooseArtistsScreen(
		onItemPlateClick = { _, _ -> },
		uiStateDispatcher = UiStateDispatcher(UserSettingsStore(context)),
		onNextButtonClick = {},
		onSearchFieldChange = {},
		chosenArtists = emptyList(),
		lazyGridState = rememberLazyGridState()
	)
}