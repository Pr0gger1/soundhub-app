package com.soundhub.ui.messenger.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.UiEventDispatcher
import com.soundhub.utils.UiEvent

@Composable
fun MessengerSearchTopBarButton(
    uiEventDispatcher: UiEventDispatcher = hiltViewModel()
) {
    IconButton(onClick = { uiEventDispatcher.sendUiEvent(UiEvent.SearchButtonClick) }) {
        Icon(Icons.Rounded.Search, contentDescription = null)
    }

}


@Composable
@Preview(name = "MessengerSearchButton", showBackground = true)
fun MessengerSearchTopBarButtonPreview() {
    MessengerSearchTopBarButton()
}