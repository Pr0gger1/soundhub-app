package com.soundhub.data.api.responses

import java.util.UUID

data class ReceivedMessageResponse(
	val id: UUID,
	val senderId: UUID,
	val replyToMessageId: UUID?,
	val chatId: UUID,
	val content: String
)
