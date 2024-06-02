package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.model.User
import com.soundhub.ui.profile.ProfileUiState
import com.soundhub.ui.profile.ProfileViewModel
import com.soundhub.ui.profile.components.sections.user_actions.buttons.DeleteFriendButton
import com.soundhub.ui.profile.components.sections.user_actions.buttons.EditProfileButton
import com.soundhub.ui.profile.components.sections.user_actions.buttons.SendFriendRequestButton
import com.soundhub.ui.profile.components.sections.user_actions.buttons.WriteMessageButton
import com.soundhub.ui.profile.components.sections.user_actions.buttons.WritePostButton
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
internal fun ProfileButtonsSection(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    uiStateDispatcher: UiStateDispatcher,
    isOriginProfile: Boolean,
    profileOwner: User?,
) {
    val profileUiState: ProfileUiState by profileViewModel.profileUiState.collectAsState()
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val authorizedUser: User? = uiState.authorizedUser

    val isUserAFriendToAuthorizedUser: Boolean = profileUiState.isUserAFriendToAuthorizedUser

    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        if (isOriginProfile) {
            EditProfileButton(
                modifier = Modifier.weight(1f),
                navController = navController,
            )
            WritePostButton(navController = navController)
        }
        else {
            WriteMessageButton(
                modifier = Modifier.weight(1f),
                profileViewModel = profileViewModel,
                navController = navController,
                user = profileOwner,
            )

            if (isUserAFriendToAuthorizedUser)
                DeleteFriendButton(
                    profileViewModel = profileViewModel,
                    authorizedUser = authorizedUser,
                    user = profileOwner
                )
            else SendFriendRequestButton(
                profileViewModel = profileViewModel,
                uiStateDispatcher = uiStateDispatcher,
                profileOwner = profileOwner
            )
        }
    }
}