package com.soundhub.ui.messenger.chat.components.input_box

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.ui.messenger.chat.ChatUiState
import com.soundhub.ui.messenger.chat.ChatViewModel
import kotlinx.coroutines.launch

@Composable
fun SendMessageButton(
    chatViewModel: ChatViewModel,
    messageContent: MutableState<String>,
    messageCount: Int,
    lazyListState: LazyListState,
    authorizedUser: User?
) {
    val scope = rememberCoroutineScope()
    val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()

    IconButton(
        enabled = messageContent.value.isNotEmpty(),
        onClick = {
        chatViewModel.sendMessage(Message(
            content = messageContent.value,
            sender = authorizedUser,
            chat = chatUiState.chat
        ))

        messageContent.value = ""
        scope.launch {
            lazyListState.scrollToItem(messageCount)
        }
    }) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.Send,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}