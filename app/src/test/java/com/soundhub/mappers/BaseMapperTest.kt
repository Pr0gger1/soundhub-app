package com.soundhub.mappers

import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.api.requests.SendMessageRequest
import com.soundhub.domain.enums.Gender
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Chat
import com.soundhub.domain.model.Genre
import com.soundhub.domain.model.Post
import com.soundhub.domain.model.User
import com.soundhub.domain.states.PostEditorState
import com.soundhub.domain.states.RegistrationState
import com.soundhub.domain.states.UserFormState
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

open class BaseMapperTest {
	private val userAvatar: String = "avatar.jpg"
	protected val artist =
		Artist(
			name = "Rammstein",
			id = UUID.randomUUID(),
			cover = "https://some.link/cover.jpg",
			genre = listOf("Metal"),
		)

	protected val user = User(
		email = "user@mail.com",
		avatarUrl = userAvatar,
		firstName = "Ivan",
		lastName = "Ivanov",
		gender = Gender.MALE,
		country = "Russia",
		city = "Moscow",
		birthday = LocalDate.now(),
		description = "description",
		languages = mutableListOf("Russian"),
		favoriteArtists = mutableListOf(artist),
		favoriteArtistsIds = listOf(artist.id),
		favoriteGenres = mutableListOf(Genre(name = "Industrial metal")),
		friends = listOf(User(firstName = "user1"), User(firstName = "user2"))
	)

	protected val registerUser: User = user.copy(friends = emptyList())

	protected val registerState: RegistrationState = RegistrationState(
		email = registerUser.email,
		password = "11111111",
		firstName = registerUser.firstName,
		avatarUrl = registerUser.avatarUrl,
		lastName = registerUser.lastName,
		gender = registerUser.gender,
		country = registerUser.country,
		city = registerUser.city,
		birthday = registerUser.birthday,
		description = registerUser.description,
		languages = registerUser.languages,
		favoriteGenres = registerUser.favoriteGenres,
		favoriteArtists = registerUser.favoriteArtists,
		id = registerUser.id
	)

	protected val userFormState: UserFormState = UserFormState(
		email = user.email,
		avatarUrl = user.avatarUrl,
		firstName = user.firstName,
		lastName = user.lastName,
		gender = user.gender,
		country = user.country,
		city = user.city,
		birthday = user.birthday,
		description = user.description,
		languages = user.languages,
		id = user.id
	)

	protected val registerRequestBody = RegisterRequestBody(
		email = registerUser.email,
		password = "11111111",
		avatarUrl = registerUser.avatarUrl,
		firstName = registerUser.firstName,
		lastName = registerUser.lastName,
		gender = registerUser.gender,
		country = registerUser.country,
		city = registerUser.city,
		birthday = registerUser.birthday,
		description = registerUser.description,
		languages = registerUser.languages,
		favoriteGenres = registerUser.favoriteGenres,
		favoriteArtistsIds = user.favoriteArtists.map { it.id }
	)

	protected val replyToMessageId: UUID = UUID.randomUUID()

	// message mapper constants
	protected val chat = Chat(createdBy = user)
	protected val messageContent = "test message"
	protected val messageRequest = SendMessageRequest(
		chatId = chat.id,
		userId = user.id,
		replyToMessageId = replyToMessageId,
		content = messageContent
	)

	// post mapper constants
	private val postContent = "test post content"
	private val postImages = listOf("image_url1", "image_url2")
	private val publishTime = LocalDateTime.now()

	private val postId = UUID.randomUUID()
	protected val post = Post(
		createdAt = publishTime,
		id = postId,
		author = user,
		content = postContent,
		images = postImages
	)
	protected val postEditorState = PostEditorState(
		createdAt = publishTime,
		postId = postId,
		content = postContent,
		author = user,
		images = postImages,
	)
}