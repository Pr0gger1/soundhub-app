package com.soundhub.ui.pages.friends.components.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.User
import com.soundhub.data.states.FriendsUiState
import com.soundhub.ui.pages.friends.FriendsViewModel
import com.soundhub.ui.pages.friends.components.UserFriendsContainer
import com.soundhub.ui.pages.friends.enums.FriendListPage
import com.soundhub.ui.shared.loaders.CircleLoader
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
internal fun UserRecommendationsPage(
	friendsViewModel: FriendsViewModel,
	uiStateDispatcher: UiStateDispatcher,
	navController: NavHostController,
	tabs: List<FriendListPage>,
	page: Int
) {
	val friendsUiState: FriendsUiState by friendsViewModel.friendsUiState.collectAsState()
	val recommendedUsers: List<User> = friendsUiState.recommendedFriends
	var filteredUserList by rememberSaveable { mutableStateOf(recommendedUsers) }
	val searchBarText: String = uiStateDispatcher.getSearchBarText()

	LaunchedEffect(key1 = searchBarText) {
		filteredUserList = friendsViewModel.filterFriendsList(
			occurrenceString = searchBarText,
			users = recommendedUsers
		)
	}

	LaunchedEffect(key1 = recommendedUsers) {
		friendsViewModel.loadUsersCompatibility(recommendedUsers.map { it.id })
	}

	when (friendsUiState.status) {
		ApiStatus.LOADING -> Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center
		) { CircleLoader(modifier = Modifier.size(64.dp)) }

		ApiStatus.ERROR -> {
			Text(
				text = stringResource(id = R.string.friends_recommended_friends_error_message),
				textAlign = TextAlign.Center,
				fontSize = 20.sp,
				fontWeight = FontWeight.Bold
			)
		}

		ApiStatus.SUCCESS -> {
			UserFriendsContainer(
				friendList = recommendedUsers,
				navController = navController,
				chosenPage = tabs[page],
				friendsViewModel = friendsViewModel
			)
		}

		else -> {}
	}
}