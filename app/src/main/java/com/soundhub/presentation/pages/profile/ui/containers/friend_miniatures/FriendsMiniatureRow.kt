package com.soundhub.presentation.pages.profile.ui.containers.friend_miniatures

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.soundhub.domain.model.User
import com.soundhub.domain.states.ProfileUiState
import com.soundhub.presentation.pages.profile.ProfileViewModel
import com.soundhub.presentation.shared.avatar.CircularAvatar

@Composable
internal fun FriendsMiniaturesRow(profileViewModel: ProfileViewModel) {
	val profileUiState: ProfileUiState by profileViewModel.profileUiState.collectAsState()
	val profileOwner: User? = profileUiState.profileOwner
	val friendList: List<User> = profileOwner?.friends.orEmpty()
	val maxFriendsMiniatureCount = 10
	var friendCount: Int by remember { mutableIntStateOf(friendList.size) }

	LaunchedEffect(key1 = friendList) {
		friendCount = if (friendList.size > maxFriendsMiniatureCount)
			(maxFriendsMiniatureCount - 1)
		else friendList.size
	}

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(start = 10.dp),
		horizontalArrangement = Arrangement.spacedBy((-15).dp)
	) {
		if (friendList.isNotEmpty()) {
			friendList.subList(0, friendCount)
				.forEach { friend ->
					CircularAvatar(
						imageUri = friend.avatarUrl?.toUri(),
						modifier = Modifier.size(24.dp)
					)
				}
		}
	}
}