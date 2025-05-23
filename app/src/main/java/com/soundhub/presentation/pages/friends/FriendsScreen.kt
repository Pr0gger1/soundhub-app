package com.soundhub.presentation.pages.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.pages.friends.enums.FriendListPage
import com.soundhub.presentation.pages.friends.ui.pager.FriendsScreenPager
import com.soundhub.presentation.pages.friends.ui.tabs.FriendsScreenTabs
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FriendsScreen(
	uiStateDispatcher: UiStateDispatcher,
	navController: NavHostController,
	friendsViewModel: FriendsViewModel,
	userId: UUID?
) {
	val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())

	var tabState: List<FriendListPage> by rememberSaveable { mutableStateOf(FriendsViewModel.Tabs) }

	val tabPagerState =
		rememberPagerState(initialPage = 0, pageCount = { FriendsViewModel.Tabs.size })
	val isOriginProfile = friendsViewModel.isOriginProfile()

	LaunchedEffect(key1 = userId) {
		userId?.let { friendsViewModel.loadProfileOwner(it) }
	}

	LaunchedEffect(key1 = isOriginProfile) {
		tabState = if (!isOriginProfile)
			listOf(FriendListPage.MAIN)
		else FriendsViewModel.Tabs
	}

	LaunchedEffect(key1 = tabPagerState.currentPage) {
		if (uiState.isSearchBarActive)
			uiStateDispatcher.setSearchBarActive(false)

		friendsViewModel.setCurrentTabIndex(tabPagerState.currentPage)
	}

	Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
		if (isOriginProfile) FriendsScreenTabs(
			selectedTabState = tabPagerState,
			tabs = tabState
		)

		FriendsScreenPager(
			selectedTabState = tabPagerState,
			navController = navController,
			friendsViewModel = friendsViewModel,
			uiStateDispatcher = uiStateDispatcher,
		)
	}
}