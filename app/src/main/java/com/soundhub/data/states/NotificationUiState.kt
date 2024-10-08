package com.soundhub.data.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Notification

data class NotificationUiState(
	val notifications: List<Notification> = emptyList(),
	val status: ApiStatus = ApiStatus.LOADING
)
