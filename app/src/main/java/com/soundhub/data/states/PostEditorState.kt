package com.soundhub.data.states

import com.soundhub.data.model.User
import com.soundhub.domain.model.Post
import java.time.LocalDateTime
import java.util.UUID

data class PostEditorState(
	var postId: UUID = UUID.randomUUID(),
	var content: String = "",
	var author: User? = null,
	var createdAt: LocalDateTime = LocalDateTime.now(),
	var likes: Set<User> = emptySet(),

	var images: List<String> = emptyList(),
	var newImages: List<String> = emptyList(),
	var imagesToBeDeleted: List<String> = emptyList(),

	var doesPostExist: Boolean = false,
	var oldPostState: Post? = null
)
