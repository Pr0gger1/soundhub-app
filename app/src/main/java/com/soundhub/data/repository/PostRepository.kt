package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Post
import java.util.UUID

interface PostRepository {
	suspend fun getPostById(postId: UUID): HttpResult<Post?>
	suspend fun getPostsByAuthorId(authorId: UUID): HttpResult<List<Post>>

	suspend fun addPost(post: Post): HttpResult<Post>

	suspend fun toggleLike(postId: UUID): HttpResult<Post>

	suspend fun deletePost(postId: UUID): HttpResult<UUID>

	suspend fun updatePost(
		postId: UUID,
		post: Post,
		newImages: List<String> = emptyList(),
		imagesToBeDeleted: List<String> = emptyList()
	): HttpResult<Post>
}