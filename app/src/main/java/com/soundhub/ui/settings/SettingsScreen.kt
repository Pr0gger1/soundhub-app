package com.soundhub.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.settings.components.SettingsMenu
import com.soundhub.ui.settings.components.UserCardSettings
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun SettingsScreen(
    authViewModel: AuthenticationViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    ContentContainer(modifier = Modifier.padding(vertical = 10.dp)) {
        Column {
            UserCardSettings(
                authViewModel = authViewModel,
                uiStateDispatcher = uiStateDispatcher
            )
            HorizontalDivider(thickness = 1.dp)
            SettingsMenu()
        }
    }
}