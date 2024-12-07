package com.soundhub.data.states

import com.soundhub.data.enums.Gender
import com.soundhub.data.states.interfaces.IUserDataFormState
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Genre
import java.time.LocalDate
import java.util.UUID

data class RegistrationState(
	var email: String = "",
	var password: String = "",
	override var id: UUID = UUID.randomUUID(),
	override var firstName: String = "",
	override var lastName: String = "",
	override var gender: Gender = Gender.UNKNOWN,
	override var country: String? = "",
	override var birthday: LocalDate? = null,
	override var city: String? = "",
	override var description: String? = "",
	override var avatarUrl: String? = null,
	override var languages: MutableList<String> = mutableListOf(),
	var favoriteGenres: List<Genre> = emptyList(),
	var favoriteArtists: List<Artist> = emptyList(),

	override var isFirstNameValid: Boolean = true,
	override var isLastNameValid: Boolean = true,
	override var isBirthdayValid: Boolean = true,
	var isLoading: Boolean = false,
) : IUserDataFormState
