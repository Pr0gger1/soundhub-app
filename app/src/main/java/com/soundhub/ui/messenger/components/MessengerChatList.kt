package com.soundhub.ui.messenger.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User
import com.soundhub.ui.messenger.EmptyMessengerScreen
import com.soundhub.ui.messenger.MessengerUiState
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
internal fun MessengerChatList(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher,
    messengerViewModel: MessengerViewModel
) {
    val messengerUiState: MessengerUiState by messengerViewModel
        .messengerUiState
        .collectAsState()

    val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val authorizedUser: User? = uiState.authorizedUser

    val chats: List<Chat> = messengerUiState.chats
    val searchBarText: String = uiState.searchBarText
    var filteredChats: List<Chat> by rememberSaveable { mutableStateOf(chats) }

    LaunchedEffect(key1 = true) {
        messengerViewModel.loadChats()
    }

    LaunchedEffect(key1 = chats) {
        messengerViewModel.updateUnreadMessageCount()
    }

    LaunchedEffect(key1 = searchBarText, key2 = chats) {
        Log.d("MessengerChatList", "searchbar text: $searchBarText")
        Log.d("MessengerChatList", "chats: $chats")
        filteredChats = messengerViewModel.filterChats(
            chats = chats,
            searchBarText = searchBarText,
            authorizedUser = authorizedUser
        )
    }

    if (filteredChats.isEmpty())
        EmptyMessengerScreen(navController = navController)
    else LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = filteredChats, key = { it.id }) { chat ->
            ChatCard(
                chat = chat,
                navController = navController,
                uiStateDispatcher = uiStateDispatcher,
                messengerViewModel = messengerViewModel
            )
        }
    }
}