package com.soundhub.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soundhub.UiEventDispatcher
import com.soundhub.utils.Route
import com.soundhub.utils.UiEvent


@Composable
fun BottomNavigationBar(items: List<NavigationItemApp>, navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var selectedItem: Route by remember { mutableStateOf(
        Route.valueOf(currentRoute) ?: Route.Postline
    )}

    LaunchedEffect(currentRoute) {
        selectedItem = Route.valueOf(currentRoute) ?: Route.Postline
    }

    val uiEventDispatcher: UiEventDispatcher = hiltViewModel()

    NavigationBar(
        modifier = Modifier
            .padding(top = 0.dp, bottom = 10.dp, start = 16.dp, end = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(
                elevation = 4.dp,
                spotColor = Color(0x40000000),
                ambientColor = Color(0x40000000)
            ),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = Color.Cyan
    ) {
        items.forEach{ item ->
            NavigationBarItem(
                icon = item.icon,
                selected = selectedItem == item.route,
                onClick = {
                    selectedItem = item.route
                    uiEventDispatcher.sendUiEvent(UiEvent.Navigate(item.route))
                    navController.navigate(item.route.route) {
                        popUpTo(Route.Postline.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

data class NavigationItemApp(
    val route: Route = Route.Postline,
    val icon: @Composable () -> Unit,
    val label: String? = "",
)