package com.soundhub.domain.states

import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.Invite
import com.soundhub.domain.model.Post
import com.soundhub.domain.model.User

data class ProfileUiState(
	val profileOwner: User? = null,
	val isRequestSent: Boolean = false,
	val isUserAFriendToAuthorizedUser: Boolean = false,
	val inviteSentByCurrentUser: Invite? = null,
	val userCreds: UserPreferences? = null,
	val userPosts: List<Post> = emptyList(),
	val postStatus: ApiStatus = ApiStatus.LOADING
)
