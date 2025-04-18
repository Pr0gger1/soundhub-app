package com.soundhub.presentation.shared.bars.top.chatbar.actions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import com.soundhub.R
import com.soundhub.domain.model.Message
import com.soundhub.domain.model.User
import com.soundhub.domain.states.ChatUiState
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.pages.chat.ChatViewModel
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
internal fun ChatTopBarCheckMessagesActions(
	chatViewModel: ChatViewModel,
	uiStateDispatcher: UiStateDispatcher
) {
	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val checkedMessages: Set<Message> = chatUiState.checkedMessages
	val authorizedUser: User? = uiState.authorizedUser

	val hasOnlyOwnMessages: Boolean = remember(checkedMessages, authorizedUser) {
		checkedMessages.all { it.author?.id == authorizedUser?.id }
	}

	val hasOnlyOneMessage: Boolean = remember(checkedMessages) {
		checkedMessages.size == 1
	}

	Row {
		AnimatedVisibility(visible = hasOnlyOwnMessages) {
			IconButton(
				onClick = {
					authorizedUser?.let { user ->
						checkedMessages.forEach { message ->
							chatViewModel.deleteMessage(message, user.id)
						}
					}.also { chatViewModel.setCheckMessageMode(false) }
				}
			) {
				Icon(
					imageVector = Icons.Rounded.Delete,
					contentDescription = "Delete message option"
				)
			}
		}

		AnimatedVisibility(visible = hasOnlyOneMessage) {
			IconButton(
				onClick = chatViewModel::activateReplyMessageMode
			) {
				Icon(
					painter = painterResource(R.drawable.round_reply_24),
					contentDescription = "Reply message option"
				)
			}
		}
	}
}