package com.soundhub.ui.states

import com.soundhub.data.model.Artist
import com.soundhub.data.model.Gender
import com.soundhub.data.model.Genre
import com.soundhub.ui.components.forms.IUserDataFormState
import java.time.LocalDate
import java.util.UUID

data class RegistrationState(
    var email: String = "",
    var password: String = "",
    override var id: UUID = UUID.randomUUID(),
    override var firstName: String? = "",
    override var lastName: String? = "",
    override var gender: Gender = Gender.UNKNOWN,
    override var country: String? = "",
    override var birthday: LocalDate? = null,
    override var city: String? = "",
    override var description: String? = "",
    override var avatarUrl: String? = null,
    override var languages: MutableList<String> = mutableListOf(),
    var favoriteGenres: MutableList<Genre> = mutableListOf(),
    var favoriteArtists: MutableList<Artist> = mutableListOf(),

    override var isFirstNameValid: Boolean = true,
    override var isLastNameValid: Boolean = true,
    override var isBirthdayValid: Boolean = true,
    var isLoading: Boolean = false,
): IUserDataFormState
