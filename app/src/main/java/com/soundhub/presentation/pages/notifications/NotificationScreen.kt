package com.soundhub.presentation.pages.notifications

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.domain.model.Invite
import com.soundhub.domain.states.NotificationUiState
import com.soundhub.presentation.pages.notifications.ui.FriendRequestNotification
import com.soundhub.presentation.shared.containers.ContentContainer

@Composable
fun NotificationScreen(
	navController: NavHostController,
	notificationViewModel: NotificationViewModel
) {
	val notificationUiState: NotificationUiState by notificationViewModel
		.notificationUiState
		.collectAsState()

	LaunchedEffect(key1 = true) {
		notificationViewModel.loadInvites()
	}


	ContentContainer(
		modifier = Modifier.padding(top = 10.dp),
		contentAlignment = if (notificationUiState.notifications.isEmpty()) Alignment.Center
		else Alignment.TopStart
	) {
		if (notificationUiState.notifications.isEmpty()) Text(
			text = stringResource(id = R.string.empty_notification_screen),
			textAlign = TextAlign.Center,
			fontSize = 20.sp,
			fontWeight = FontWeight.Bold
		)

		LazyColumn(
			modifier = Modifier.fillMaxSize()
		) {
			items(items = notificationUiState.notifications, key = { it.id }) { item ->
				when (item) {
					is Invite ->
						FriendRequestNotification(
							invite = item,
							notificationViewModel = notificationViewModel,
							navController = navController
						)
				}
			}
		}
	}
}