package com.soundhub.ui.shared.bars.bottom

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.data.states.UiState
import com.soundhub.ui.pages.messenger.MessengerViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import kotlinx.coroutines.flow.merge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
	navController: NavController,
	messengerViewModel: MessengerViewModel,
	uiStateDispatcher: UiStateDispatcher,
	navBarViewModel: NavBarViewModel = hiltViewModel()
) {
	val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val authorizedUser: User? = uiState.authorizedUser
	val navRoute: NavBackStackEntry? by navController.currentBackStackEntryAsState()

	val selectedItemState by navBarViewModel.selectedItem.collectAsState()
	val navBarMenuItems: List<NavBarMenuItem> = remember(authorizedUser?.id) {
		navBarViewModel.getNavBarItems(authorizedUser?.id)
	}

	val navBarRoutes: List<String> = remember(navBarMenuItems) { navBarMenuItems.map { it.route } }

	LaunchedEffect(key1 = uiState.currentRoute, key2 = navRoute) {
		val checkRoute: (String) -> Boolean = { route ->
			uiState.currentRoute?.contains(route) == true
		}
		val isInRoutesOrContains: Boolean = selectedItemState in navBarRoutes ||
				navBarRoutes.any { checkRoute(it) }

		val selectedItem = if (isInRoutesOrContains) {
			navBarRoutes.firstOrNull { checkRoute(it) }
		} else null

		navBarViewModel.setSelectedItem(selectedItem)
		Log.d("BottomNavigationBar", "selected page: $selectedItemState")
	}

	NavigationBar(
		modifier = Modifier
			.padding(vertical = 10.dp, horizontal = 16.dp)
			.clip(RoundedCornerShape(16.dp))
			.shadow(
				elevation = 4.dp,
				spotColor = Color(0x40000000),
				ambientColor = Color(0x40000000)
			),
		containerColor = MaterialTheme.colorScheme.primaryContainer,
		contentColor = MaterialTheme.colorScheme.primary,
	) {
		navBarMenuItems.forEach { menuItem ->
			Log.d("BottomNavigationBar", "menuItem: $menuItem")
			NavigationBarItem(
				icon = {
					NavBarItemBadgeBox(
						messengerViewModel = messengerViewModel,
						uiStateDispatcher = uiStateDispatcher,
						menuItem = menuItem
					)
				},
				selected = selectedItemState == menuItem.route,
				onClick = { navBarViewModel.onMenuItemClick(menuItem, navController) }
			)
		}
	}
}

@Composable
private fun NavBarItemBadgeBox(
	messengerViewModel: MessengerViewModel,
	uiStateDispatcher: UiStateDispatcher,
	menuItem: NavBarMenuItem,
) {
	val receivedMessageChannel = uiStateDispatcher.receivedMessages
	val readMessagesChannel = uiStateDispatcher.receivedMessages
	var unreadMessageCount by rememberSaveable { mutableIntStateOf(0) }

	LaunchedEffect(key1 = receivedMessageChannel, key2 = readMessagesChannel) {
		unreadMessageCount = messengerViewModel.getUnreadChatCount()
		merge(receivedMessageChannel, readMessagesChannel).collect { _ ->
			unreadMessageCount = messengerViewModel.getUnreadChatCount()
		}
	}

	BadgedBox(
		badge = {
			if (unreadMessageCount > 0 && menuItem.route == Route.Messenger.route)
				Badge { Text(text = unreadMessageCount.toString()) }
		}
	) { NavBarIcon(menuItem.icon, contentDescription = menuItem.route) }
}