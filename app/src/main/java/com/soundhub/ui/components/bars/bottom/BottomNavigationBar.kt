package com.soundhub.ui.components.bars.bottom

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.soundhub.ui.components.icons.QueueMusic
import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import java.util.UUID

@Composable
fun BottomNavigationBar(
    navController: NavController,
    messengerViewModel: MessengerViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val authorizedUser: User? = uiState.authorizedUser

    val defaultSelectedItem = uiState.currentRoute ?: Route.PostLine.route
    val selectedItemState: MutableState<String> = remember { mutableStateOf(defaultSelectedItem) }

    val messengerUiState by messengerViewModel.messengerUiState.collectAsState()
    val receivedMessageChannel = uiStateDispatcher.receivedMessages

    LaunchedEffect(key1 = true) {
        receivedMessageChannel.collect { msg ->
            messengerViewModel.updateUnreadMessageCount()
        }
    }

    val navBarItems: List<NavBarItem> = getNavBarItems(authorizedUser?.id)

    LaunchedEffect(key1 = selectedItemState.value) {
        val stringNavBarItems: List<String> = navBarItems.map { it.route }
        if (selectedItemState.value !in stringNavBarItems)
            selectedItemState.value = Route.PostLine.route
        Log.d("BottomNavigationBar", selectedItemState.value)
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
        navBarItems.forEach { menuItem ->
            Log.d("BottomNavigationBar", "menuItem: $menuItem")
            NavigationBarItem(
                icon = {
                    BadgedBox(
                        badge = {
                            if (messengerUiState.unreadMessagesCount > 0 && menuItem.route == Route.Messenger.route)
                                Badge { Text(text = messengerUiState.unreadMessagesCount.toString()) }
                        }
                    ) { Icon(menuItem.icon, contentDescription = menuItem.route) }
                },
                selected = selectedItemState.value == menuItem.route,
                onClick = { onMenuItemClick(selectedItemState, menuItem, navController) }
            )
        }
    }
}

private fun onMenuItemClick(
    selectedItemState: MutableState<String>,
    menuItem: NavBarItem,
    navController: NavController
) {
    selectedItemState.value = menuItem.route
    Log.d("BottomNavigationBar", "onMenuItemClick: ${menuItem.route}")

    navController.navigate(menuItem.route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
            inclusive = true
        }

        launchSingleTop = true
        restoreState = true
    }
}


private fun getNavBarItems(userId: UUID?): List<NavBarItem> {
    val navBarButtons = mutableListOf(
        NavBarItem(
            route = Route.PostLine.route,
            icon = Icons.Rounded.Home,
        ),
        NavBarItem(
            route = Route.Music.route,
            icon = Icons.Rounded.QueueMusic,
        ),
        NavBarItem(
            route = Route.Messenger.route,
            icon = Icons.Rounded.Email
        )
    )

    userId?.let {
        navBarButtons.add(
            NavBarItem(
                route = Route.Profile.getStringRouteWithNavArg(userId.toString()),
                icon = Icons.Rounded.AccountCircle
            )
        )
    }

    return navBarButtons
}