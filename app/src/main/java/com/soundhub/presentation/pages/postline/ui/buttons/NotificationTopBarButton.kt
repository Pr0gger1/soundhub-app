package com.soundhub.presentation.pages.postline.ui.buttons

import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.Route
import com.soundhub.domain.model.Notification
import com.soundhub.domain.states.NotificationUiState
import com.soundhub.presentation.pages.notifications.NotificationViewModel

@Composable
fun NotificationTopBarButton(
	navController: NavHostController,
	notificationViewModel: NotificationViewModel
) {
	val notificationUiState: NotificationUiState by notificationViewModel
		.notificationUiState.collectAsState()

	val notifications: List<Notification> = notificationUiState.notifications

	IconButton(onClick = { navController.navigate(Route.Notifications.route) }) {
		BadgedBox(badge = {
			if (notifications.isNotEmpty())
				Badge(
					modifier = Modifier.offset(x = (-20).dp)
				) {
					Text(text = notifications.size.toString())
				}
		}) {
			Icon(Icons.Rounded.Notifications, contentDescription = "notification_button")
		}

	}
}