package com.soundhub.presentation.pages.messenger.ui.state_layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.domain.model.User
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
internal fun EmptyMessengerScreen(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher
) {
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val authorizedUser: User? = uiState.authorizedUser

	Column(
		modifier = modifier.fillMaxSize(),
		verticalArrangement = Arrangement.spacedBy(
			space = 10.dp,
			alignment = Alignment.CenterVertically
		),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = stringResource(id = R.string.empty_messenger_screen),
			fontSize = 20.sp,
			fontWeight = FontWeight.Light
		)

		OutlinedButton(
			onClick = {
				val userId: String = authorizedUser?.id.toString()
				val route: String = Route.Profile.Friends.getStringRouteWithNavArg(userId)
				navController.navigate(route)
			},
			shape = RoundedCornerShape(10.dp)
		) {
			Text(
				text = stringResource(id = R.string.empty_messenger_screen_button),
				fontSize = 16.sp
			)
		}
	}
}