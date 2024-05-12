package com.soundhub.ui.music_preferences

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Artist
import com.soundhub.ui.authentication.postregistration.components.MusicItemPlate
import com.soundhub.ui.authentication.postregistration.states.ArtistUiState
import com.soundhub.ui.components.buttons.FloatingNextButton
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.components.loaders.CircleLoader
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun ChooseArtistsScreen(
    artistState: ArtistUiState,
    onItemPlateClick: (isChosen: Boolean, artist: Artist) -> Unit,
    uiStateDispatcher: UiStateDispatcher,
    onNextButtonClick: () -> Unit,
    onSearchFieldChange: (value: String) -> Unit,
    lazyGridState: LazyGridState
) {
    val uiState by uiStateDispatcher.uiState.collectAsState()

    ContentContainer(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 20.dp, end = 15.dp, bottom = 30.dp),
                text = stringResource(id = R.string.screen_title_choose_artists),
                fontSize = 32.sp,
                lineHeight = 42.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.ExtraBold
            )

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

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                contentPadding = PaddingValues(all = 10.dp),
                state = lazyGridState
            ) {
                items(items = artistState.artists, key = { it.id }) { artist ->
                    MusicItemPlate(
                        modifier = Modifier.padding(bottom = 20.dp),
                        caption = artist.title ?: "",
                        thumbnailUrl = artist.thumb,
                        onClick = { isChosen -> onItemPlateClick(isChosen, artist) },
                        isChosen = artist in artistState.chosenArtists,
                        width = 90.dp,
                        height = 90.dp
                    )
                }
            }

            if (artistState.artists.isEmpty() || artistState.status == ApiStatus.LOADING)
                CircleLoader(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(vertical = 20.dp),
                    strokeWidth = 7.dp
                )
        }

        FloatingNextButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = onNextButtonClick
        )
    }
}

@Composable
@Preview(showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
            or Configuration.UI_MODE_TYPE_NORMAL
)
private fun ChooseArtistsPreview() {
    ChooseArtistsScreen(
        artistState = ArtistUiState(),
        onItemPlateClick = {_, _ ->  },
        uiStateDispatcher = UiStateDispatcher(),
        onNextButtonClick = {},
        onSearchFieldChange = {},
        lazyGridState = rememberLazyGridState()
    )
}