package com.soundhub.domain.usecases.chat

import android.util.Log
import com.soundhub.data.api.requests.CreateChatRequestBody
import com.soundhub.domain.model.Chat
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.ChatRepository
import java.util.UUID
import javax.inject.Inject

class GetOrCreateChatByUserUseCase @Inject constructor(
	private val chatRepository: ChatRepository
) {
	suspend operator fun invoke(
		interlocutor: User?,
		userId: UUID
	): Chat? {
		if (interlocutor == null)
			return null

		val allChats: List<Chat> = chatRepository
			.getAllChatsByUserId(userId)
			.onFailure { Log.e("GetOrCreateChatUseCase", "get all chats error: $it") }
			.getOrNull()
			.orEmpty()

		val isChatExists: Boolean = allChats.any { chat -> interlocutor in chat.participants }

		return if (!isChatExists)
			chatRepository.createChat(
				body = CreateChatRequestBody(interlocutor.id)
			).onFailure { Log.e("GetOrCreateChatUseCase", "create chat error: $it") }
				.getOrNull()
		else allChats.first { interlocutor in it.participants }
	}
}