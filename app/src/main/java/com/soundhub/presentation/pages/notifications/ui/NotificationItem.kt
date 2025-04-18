package com.soundhub.presentation.pages.notifications.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.soundhub.domain.model.Notification

@Composable
fun NotificationItem(
	modifier: Modifier = Modifier,
	notification: Notification,
	onClick: () -> Unit = {},
	content: @Composable () -> Unit
) {
	ElevatedCard(
		shape = RoundedCornerShape(10.dp),
		colors = CardDefaults.cardColors(),
		modifier = modifier
			.fillMaxWidth()
			.shadow(
				elevation = 5.dp,
				ambientColor = MaterialTheme.colorScheme.onSurface,
				shape = RoundedCornerShape(10.dp)
			),
		onClick = onClick
	) {
		Box(modifier = Modifier.padding(20.dp)) {
			content()
		}
	}
}