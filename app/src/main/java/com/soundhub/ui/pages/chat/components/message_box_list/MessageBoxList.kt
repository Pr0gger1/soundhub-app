package com.soundhub.ui.pages.chat.components.message_box_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.data.states.ChatUiState
import com.soundhub.data.states.UiState
import com.soundhub.ui.pages.chat.ChatViewModel
import com.soundhub.ui.pages.chat.components.message_box.MessageBox
import com.soundhub.ui.shared.pagination.PagingLoadStateContainer
import com.soundhub.ui.viewmodels.UiStateDispatcher
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBoxList(
	modifier: Modifier = Modifier,
	chatViewModel: ChatViewModel,
	uiStateDispatcher: UiStateDispatcher,
	lazyListState: LazyListState
) {
	val coroutineScope = rememberCoroutineScope()
	var initialScroll by remember { mutableStateOf(false) }
	var currentDate: LocalDate? by remember { mutableStateOf(null) }
	val layoutInfo by remember { derivedStateOf { lazyListState.layoutInfo } }
	val totalItemCount = layoutInfo.totalItemsCount

	var unreadMessageCount: Int by remember { mutableIntStateOf(0) }

	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
	val currentChat: Chat? = chatUiState.chat

	val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val authorizedUser: User? = uiState.authorizedUser

	val pagedMessages = remember(currentChat) {
		chatViewModel.getPagedMessages()
	}.collectAsLazyPagingItems()

	val messageSnapshot: List<Message> = pagedMessages.itemSnapshotList
		.toList()
		.filterNotNull()

	val receivedMessageChannel = uiStateDispatcher.receivedMessages
	val deletedMessageChannel = uiStateDispatcher.deletedMessages
	val readMessageChannel = uiStateDispatcher.readMessages

	val loadState = pagedMessages.loadState
	val firstVisibleMessageIndex: Int by remember {
		derivedStateOf { lazyListState.firstVisibleItemIndex }
	}

	LaunchedEffect(key1 = messageSnapshot) {
		chatViewModel.cacheMessages(messageSnapshot)

		unreadMessageCount = messageSnapshot.count {
			it.isNotReadAndNotSentBy(authorizedUser)
		}
	}

	LaunchedEffect(key1 = totalItemCount, key2 = initialScroll) {
		if (!initialScroll) {
			chatViewModel.scrollToLastMessage(reverse = true)
			initialScroll = true
		}
	}

	LaunchedEffect(
		key1 = receivedMessageChannel,
		key2 = readMessageChannel,
		key3 = deletedMessageChannel
	) {
		merge(receivedMessageChannel, readMessageChannel, deletedMessageChannel)
			.collect { pagedMessages.refresh() }
	}

	LaunchedEffect(
		key1 = firstVisibleMessageIndex,
		key2 = totalItemCount,
	) {
		val messages = pagedMessages.itemSnapshotList
			.toList()
			.filterNotNull()

			chatViewModel.readVisibleMessagesFromIndex(
				firstVisibleMessageIndex,
				messages
			)
	}

	Box(modifier = modifier.fillMaxSize()) {
		PagingLoadStateContainer(loadState)
		LazyColumn(
			state = lazyListState,
			reverseLayout = true,
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.spacedBy(10.dp),
		) {
			stickyHeader { currentDate?.let { MessageDateChip(it) } }
			items(pagedMessages.itemCount, key = { pagedMessages.peek(it)?.id ?: it }) { index ->
				pagedMessages[index]?.let { message ->
					val messageDate = message.timestamp.toLocalDate()
					if (messageDate != currentDate)
						currentDate = messageDate

					MessageBox(
						modifier = Modifier,
						message = message,
						isOwnMessage = message.sender?.id == authorizedUser?.id,
						chatViewModel = chatViewModel,
					)
				}
			}
		}


		AnimatedVisibility(
			visible = lazyListState.canScrollBackward,
			enter = scaleIn(),
			exit = scaleOut(),
			modifier = Modifier
				.align(Alignment.BottomStart)
				.padding(bottom = 10.dp),
		) {
			BadgedBox(
				badge = {
					if (unreadMessageCount > 0)
						Badge { Text(unreadMessageCount.toString()) }
				}
			) {
				FloatingActionButton(
					containerColor = MaterialTheme.colorScheme.tertiaryContainer,
					onClick = {
						coroutineScope.launch {
							chatViewModel.animateScrollToLastMessage(reverse = true)
						}
					}
				) {
					Icon(
						imageVector = Icons.Rounded.KeyboardArrowDown,
						contentDescription = "scroll down"
					)
				}
			}
		}
	}
}