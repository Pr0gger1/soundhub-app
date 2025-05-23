package com.soundhub.presentation.pages.profile.ui.sections.avatar

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.domain.states.IProfileUiState
import com.soundhub.presentation.shared.menu.AvatarDropdownMenu
import com.soundhub.presentation.viewmodels.IProfileViewModel

@Composable
internal fun <T> UserProfileAvatar(
	navController: NavHostController,
	profileViewModel: IProfileViewModel<T>,
) {
	var selectedImageUri: Uri? by rememberSaveable { mutableStateOf(null) }
	val isAvatarMenuExpandedState = rememberSaveable { mutableStateOf(false) }
	val changeAvatarLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent()
	) { uri -> uri?.let { selectedImageUri = it } }

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.fillMaxHeight(0.45f)
	) {
		Avatar(
			isAvatarMenuExpandedState = isAvatarMenuExpandedState,
			profileViewModel = profileViewModel
		)
		AvatarDropdownMenu(
			modifier = Modifier.align(Alignment.Center),
			isAvatarMenuExpandedState = isAvatarMenuExpandedState,
			onDismissRequest = { isAvatarMenuExpandedState.value = false },
			activityResultLauncher = changeAvatarLauncher
		)
		UserSettingsButton(navController)
	}
}

@Composable
private fun UserSettingsButton(
	navController: NavHostController
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 20.dp, start = 16.dp, end = 16.dp),
		horizontalArrangement = Arrangement.End
	) {
		IconButton(
			onClick = { navController.navigate(Route.Settings.route) },
			modifier = Modifier
				.background(
					MaterialTheme.colorScheme.primaryContainer,
					shape = CircleShape
				)
		) {
			Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
		}
	}
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun <T> Avatar(
	isAvatarMenuExpandedState: MutableState<Boolean>,
	profileViewModel: IProfileViewModel<T>
) {
	val profileUiState: IProfileUiState<T> by profileViewModel.profileUiState.collectAsState()
	val profileOwner: T? = profileUiState.profileOwner

	val userFullName: String = profileViewModel.getUserName()
	val modelUrl: Any? = remember(profileOwner) { profileViewModel.getAvatarModel() }
	val defaultAvatar: Painter = painterResource(id = R.drawable.user_placeholder)

	AsyncImage(
		model = modelUrl,
		contentScale = ContentScale.Crop,
		contentDescription = userFullName,
		placeholder = defaultAvatar,
		error = defaultAvatar,
		modifier = Modifier
			.fillMaxSize()
			.clickable {
				isAvatarMenuExpandedState.value = true
			},
	)
}