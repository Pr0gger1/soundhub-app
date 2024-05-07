package com.soundhub.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.model.User
import com.soundhub.ui.profile.components.sections.avatar.UserProfileAvatar
import com.soundhub.ui.profile.components.UserProfileContainer
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun ProfileScreen(
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher,
    profileViewModel: ProfileViewModel,
) {
    val profileUiState by profileViewModel.profileUiState.collectAsState()
    val profileOwner: User? = profileUiState.profileOwner
    var isLoading: Boolean by rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = profileOwner) {
        profileOwner?.let { isLoading = false }
    }

    if (!isLoading) Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background),
        // negative indent provides an overlay of the content on the avatar
        verticalArrangement = Arrangement.spacedBy((-30).dp)
    ) {
        UserProfileAvatar(
            navController = navController,
            profileViewModel = profileViewModel,
            profileOwner = profileOwner
        )

        UserProfileContainer(
            profileOwner = profileOwner,
            navController = navController,
            uiStateDispatcher = uiStateDispatcher,
            profileViewModel = profileViewModel
        )
    }
}