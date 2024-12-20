package com.soundhub.presentation.pages.friends.ui.pager.pages

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.soundhub.domain.model.User
import com.soundhub.domain.states.FriendsUiState
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.pages.friends.FriendsViewModel
import com.soundhub.presentation.pages.friends.ui.containers.UserFriendsContainer
import com.soundhub.presentation.pages.friends.ui.state_layouts.EmptyFriendsScreen
import com.soundhub.presentation.pages.friends.ui.state_layouts.UnauthorizedEmptyFriendsPage
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MainFriendsPage(
	friendsViewModel: FriendsViewModel,
	uiStateDispatcher: UiStateDispatcher,
	navController: NavHostController,
	selectedTabState: PagerState,
) {
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val friendsUiState: FriendsUiState by friendsViewModel.friendsUiState.collectAsState()
	val isOriginProfile = friendsViewModel.isOriginProfile()

	val profileOwner: User? = friendsUiState.profileOwner
	val searchBarText: String = uiState.searchBarText
	val friends: List<User> = profileOwner?.friends.orEmpty().distinct()
	var filteredFriendList: List<User> by rememberSaveable { mutableStateOf(friends) }

	LaunchedEffect(key1 = searchBarText) {
		Log.d("MainFriendsPage", "text: $searchBarText")
		filteredFriendList = friendsViewModel
			.filterFriendsList(searchBarText, friends)
	}

	LaunchedEffect(key1 = profileOwner, key2 = friends) {
		filteredFriendList = friends
	}


	if (friends.isEmpty()) {
		if (!isOriginProfile) {
			UnauthorizedEmptyFriendsPage()
		} else EmptyFriendsScreen(selectedTabState, friendsViewModel)
	} else UserFriendsContainer(
		friendList = filteredFriendList,
		navController = navController,
		friendsViewModel = friendsViewModel
	)
}