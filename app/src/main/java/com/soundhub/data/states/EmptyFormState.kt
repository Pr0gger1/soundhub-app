package com.soundhub.data.states

import com.soundhub.data.model.Gender
import com.soundhub.ui.shared.forms.IUserDataFormState
import java.time.LocalDate
import java.util.UUID

data class EmptyFormState(
	override var id: UUID = UUID.randomUUID(),
	override var firstName: String = "",
	override var lastName: String = "",
	override var gender: Gender = Gender.UNKNOWN,
	override var country: String? = null,
	override var birthday: LocalDate? = null,
	override var city: String? = null,
	override var description: String? = null,
	override var avatarUrl: String? = null,
	override var languages: MutableList<String> = mutableListOf(),
	override var isFirstNameValid: Boolean = true,
	override var isLastNameValid: Boolean = true,
	override var isBirthdayValid: Boolean = true,
) : IUserDataFormState