package com.soundhub.presentation.pages.profile.ui.sections.user_actions.buttons

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.domain.model.User
import com.soundhub.domain.states.ProfileUiState
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.pages.profile.ProfileViewModel
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import kotlinx.coroutines.launch

@Composable
internal fun SendFriendRequestButton(
	modifier: Modifier = Modifier,
	profileViewModel: ProfileViewModel,
	uiStateDispatcher: UiStateDispatcher,
	profileOwner: User?
) {
	val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val profileUiState: ProfileUiState by profileViewModel.profileUiState.collectAsState()
	val isRequestSent: Boolean = profileUiState.isRequestSent

	val coroutineScope = rememberCoroutineScope()
	var buttonIconRes by rememberSaveable { mutableIntStateOf(R.drawable.baseline_person_add) }

	LaunchedEffect(key1 = profileUiState) {
		if (profileOwner?.id != uiState.authorizedUser?.id)
			profileViewModel.checkInvite()
	}

	LaunchedEffect(key1 = isRequestSent) {
		Log.d("SendFriendRequestButton", "was request sent: $isRequestSent")
		buttonIconRes = getSendRequestBtnIconRes(isRequestSent)
	}

	FilledTonalIconToggleButton(
		modifier = modifier.size(48.dp),
		shape = RoundedCornerShape(10.dp),
		checked = isRequestSent,
		onCheckedChange = {
			coroutineScope.launch {
				profileViewModel.onSendRequestButtonClick(
					isRequestSent = isRequestSent,
					user = profileOwner
				)
			}
		},
	) {
		Icon(
			painter = painterResource(id = buttonIconRes),
			contentDescription = "add friend button"
		)
	}
}


private fun getSendRequestBtnIconRes(isRequestSent: Boolean): Int =
	if (isRequestSent)
		R.drawable.baseline_how_to_reg_24
	else
		R.drawable.baseline_person_add