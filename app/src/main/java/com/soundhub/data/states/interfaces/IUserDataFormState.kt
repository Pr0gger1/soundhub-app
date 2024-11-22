package com.soundhub.data.states.interfaces

import com.soundhub.data.enums.Gender
import java.time.LocalDate
import java.util.UUID

interface IUserDataFormState {
	var id: UUID
	var firstName: String
	var lastName: String
	var gender: Gender
	var country: String?
	var birthday: LocalDate?
	var city: String?
	var description: String?
	var avatarUrl: String?
	var languages: MutableList<String>

	var isFirstNameValid: Boolean
	var isLastNameValid: Boolean
	var isBirthdayValid: Boolean
}