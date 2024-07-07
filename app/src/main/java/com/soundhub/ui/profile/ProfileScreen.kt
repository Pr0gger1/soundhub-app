package com.soundhub.ui.profile

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.ui.profile.components.UserProfileContainer
import com.soundhub.ui.profile.components.sections.avatar.UserProfileAvatar
import com.soundhub.ui.viewmodels.UiStateDispatcher
import java.util.UUID
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    userId: UUID?,
    uiStateDispatcher: UiStateDispatcher,
    profileViewModel: ProfileViewModel,
) {
    val configuration = LocalConfiguration.current
    val scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = true,
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    val bottomSheetState = scaffoldState.bottomSheetState
    val partiallyExpandedSheetHeight: Int by remember {
        derivedStateOf { floor(configuration.screenHeightDp / 2.0).toInt() }
    }

    var targetCornerRadius by remember { mutableStateOf(30.dp) }
    val animatedCornerRadius by animateDpAsState(
        targetValue = targetCornerRadius,
        label = "bottom sheet corner animation"
    )

    LaunchedEffect(userId) {
        userId?.let { profileViewModel.loadProfileOwner(it) }
    }

    LaunchedEffect(bottomSheetState) {
        snapshotFlow { bottomSheetState.targetValue }
            .collect { sheetValue ->
                targetCornerRadius = when (sheetValue) {
                    SheetValue.Expanded -> 0.dp
                    SheetValue.PartiallyExpanded -> 30.dp
                    else -> 30.dp
                }
            }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.primaryContainer,
        sheetContent = {
            UserProfileContainer(
                navController = navController,
                uiStateDispatcher = uiStateDispatcher,
                profileViewModel = profileViewModel
            )
        },
        sheetShape = RoundedCornerShape(
            topStart = animatedCornerRadius,
            topEnd = animatedCornerRadius
        ),
        sheetPeekHeight = partiallyExpandedSheetHeight.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy((-30).dp)
        ) {
            UserProfileAvatar(
                navController = navController,
                uiStateDispatcher = uiStateDispatcher,
                profileViewModel = profileViewModel
            )
        }
    }
}

