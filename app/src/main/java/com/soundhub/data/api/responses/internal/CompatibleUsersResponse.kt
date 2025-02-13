package com.soundhub.data.api.responses.internal

import com.google.gson.annotations.SerializedName
import com.soundhub.domain.model.User

data class CompatibleUsersResponse(
	@SerializedName("userCompatibilities")
	val userCompatibilities: List<UserCompatibility> = emptyList()
)

data class UserCompatibility(
	@SerializedName("user")
	val user: User,
	val compatibility: Float
)