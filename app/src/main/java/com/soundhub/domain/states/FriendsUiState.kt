package com.soundhub.domain.states

import com.soundhub.data.api.responses.internal.CompatibleUsersResponse
import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.User
import com.soundhub.domain.states.interfaces.UiStatusState

data class FriendsUiState(
	val recommendedFriends: List<User> = emptyList(),
	val userCompatibilityPercentage: CompatibleUsersResponse? = null,
	val profileOwner: User? = null,
	val isOriginProfile: Boolean = true,
	val foundUsers: List<User> = emptyList(),
	override val status: ApiStatus = ApiStatus.LOADING,
) : UiStatusState