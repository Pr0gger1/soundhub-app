package com.soundhub.domain.usecases.chat

import android.util.Log
import com.soundhub.domain.model.Chat
import com.soundhub.domain.repository.ChatRepository
import java.util.UUID
import javax.inject.Inject

class GetAllChatsByUserUseCase @Inject constructor(
	private val chatRepository: ChatRepository
) {
	suspend operator fun invoke(userId: UUID): List<Chat> {
		var chats: List<Chat> = emptyList()
		chatRepository.getAllChatsByUserId(userId)
			.onSuccess { response -> chats = response.body.orEmpty() }
			.onFailure { Log.e("GetAllChatByUserUseCase", "error: $it") }

		return chats
	}
}