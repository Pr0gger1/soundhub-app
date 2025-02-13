package com.soundhub.data.api.requests

import com.soundhub.domain.enums.Gender
import com.soundhub.domain.model.Genre
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class RegisterRequestBody(
	var email: String = "",
	var password: String = "",
	var firstName: String = "",
	var lastName: String = "",
	var birthday: LocalDate? = null,
	var city: String? = null,
	var country: String? = null,
	var gender: Gender = Gender.UNKNOWN,
	var avatarUrl: String? = null,
	var description: String? = "",
	var languages: List<String> = emptyList(),
	var favoriteGenres: List<Genre> = emptyList(),
	var favoriteArtistsIds: List<UUID> = emptyList(),
	var online: Boolean = false,
	var lastOnline: LocalDateTime? = null
)
