package com.soundhub.ui.components.bars.bottom

import androidx.compose.runtime.Composable
import com.soundhub.Route

data class NavBarItem(
    val route: String = Route.Postline.route,
    val icon: @Composable () -> Unit,
)