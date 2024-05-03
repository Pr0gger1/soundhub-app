package com.soundhub.ui.profile.components.sections.favorite_genres

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.Route
import com.soundhub.data.model.Genre
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun GenresFlowRow(
    genreList: List<Genre> = emptyList(),
    isOriginProfile: Boolean,
    navController: NavHostController
) {
    val maxItemsPerRow = 3

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        maxItemsInEachRow = maxItemsPerRow,

        ) {
        genreList.forEach { genre ->
            genre.name?.let {
                FavoriteGenreItem(
                    modifier = Modifier
                        .wrapContentWidth()
                        .weight(1f),
                    genreName = it,
                    genreColor = generateContrastColor(MaterialTheme.colorScheme.onPrimary)
                )
            }
        }

        if (isOriginProfile) IconButton(
            onClick = { navController.navigate(Route.EditFavoriteGenres.route) },
            modifier = Modifier.size(40.dp)
        ) { Icon(Icons.Rounded.Add, contentDescription = "add genre button") }
    }
}

// TODO: remake color generation
private fun generateContrastColor(baseColor: Color): Color {
    val contrastFactor = 1f
    val r = (baseColor.red + (Random.nextFloat() - 0.5f) * contrastFactor).coerceIn(0f, 1f)
    val g = (baseColor.green + (Random.nextFloat() - 0.5f) * contrastFactor).coerceIn(0f, 1f)
    val b = (baseColor.blue + (Random.nextFloat() - 0.5f) * contrastFactor).coerceIn(0f, 1f)

    return Color(r, g, b)
}