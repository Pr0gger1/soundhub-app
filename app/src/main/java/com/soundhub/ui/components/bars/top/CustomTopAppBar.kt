package com.soundhub.ui.components.bars.top

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.UiEventDispatcher

@Composable
fun CustomTopAppBar(
    topBarTitle: String?,
    currentRoute: String?,
    uiEventDispatcher: UiEventDispatcher = hiltViewModel(),
    navController: NavHostController
) {
    topBarTitle?.let {
        AppHeader(
            modifier = Modifier.padding(0.dp),
            pageName = topBarTitle,
            actionButton = {
                DynamicCustomTopBarButton(
                    currentRoute = currentRoute,
                    navController = navController,
                    uiEventDispatcher = uiEventDispatcher
                )
            }
        )
    }
}