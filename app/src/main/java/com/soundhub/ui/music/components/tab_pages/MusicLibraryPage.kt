package com.soundhub.ui.music.components.tab_pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.ui.music.MusicViewModel

@Composable
internal fun MusicLibraryPage(
    modifier: Modifier = Modifier,
    musicViewModel: MusicViewModel,
    navController: NavHostController
) {
    val authorized = false

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MusicLibraryMenu(
            musicViewModel = musicViewModel,
            navController = navController,
            isAuthorized = authorized
        )
        if (!authorized)
            UnauthorizedLibraryActions()
    }
}