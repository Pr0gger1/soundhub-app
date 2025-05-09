package com.soundhub.presentation.pages.profile.ui.sections.favorite_genres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.Route
import com.soundhub.domain.model.Genre
import com.soundhub.domain.states.ProfileUiState
import com.soundhub.presentation.pages.profile.ProfileViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun GenresFlowRow(
	profileViewModel: ProfileViewModel,
	isOriginProfile: Boolean,
	navController: NavHostController,
	maxItemsPerRow: Int = 3
) {
	val profileUiState: ProfileUiState by profileViewModel
		.profileUiState.collectAsState()

	val genreList: List<Genre> = profileUiState.profileOwner?.favoriteGenres.orEmpty()
	val colors = remember(genreList) {
		genreList.map { genre -> generateContrastColor(genre.name ?: "") }
	}

	FlowRow(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(5.dp),
		verticalArrangement = Arrangement.spacedBy(10.dp),
		maxItemsInEachRow = maxItemsPerRow,
	) {
		genreList.forEachIndexed { index, genre ->
			genre.name?.let {
				FavoriteGenreItem(
					modifier = Modifier
						.wrapContentWidth()
						.weight(1f),
					genreName = it,
					genreColor = colors[index]
				)
			}
		}

		if (isOriginProfile)
			IconButton(
				onClick = { navController.navigate(Route.EditFavoriteGenres.route) },
				modifier = Modifier.size(40.dp)
			) {
				Icon(Icons.Rounded.Add, contentDescription = "add genre button")
			}
	}
}

private fun generateContrastColor(genreName: String): Color {
	return Color(genreName.hashCode()).copy(alpha = 0.3f)
}