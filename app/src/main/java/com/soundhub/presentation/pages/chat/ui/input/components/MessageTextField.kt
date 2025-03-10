package com.soundhub.presentation.pages.chat.ui.input.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.domain.states.ChatUiState
import com.soundhub.presentation.pages.chat.ChatViewModel

@Composable
fun MessageTextField(
	modifier: Modifier = Modifier,
	chatViewModel: ChatViewModel
) {
	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
	val messageContent: String = chatUiState.messageContent

	OutlinedTextField(
		modifier = modifier
			.height(72.dp)
			.fillMaxWidth(),
		singleLine = true,
		value = messageContent,
		onValueChange = { chatViewModel.setMessageContent(it) },
		colors = TextFieldDefaults.colors(
			focusedIndicatorColor = Color.Transparent,
			unfocusedIndicatorColor = Color.Transparent,
			focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
			unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
			unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
			focusedTextColor = MaterialTheme.colorScheme.onSurface
		),
		placeholder = {
			Column(
				modifier = Modifier.fillMaxSize(),
				verticalArrangement = Arrangement.Center
			) {
				Text(
					text = stringResource(id = R.string.message_input_placeholder),
					modifier = Modifier
				)
			}
		}
	)
}