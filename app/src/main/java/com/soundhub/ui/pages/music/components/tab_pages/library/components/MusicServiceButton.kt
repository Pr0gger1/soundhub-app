package com.soundhub.ui.pages.music.components.tab_pages.library.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog.MusicServiceDialog
import com.soundhub.ui.pages.music.viewmodels.MusicServiceDialogViewModel
import com.soundhub.ui.theme.acceptColor
import com.soundhub.ui.theme.onAcceptColor

@Composable
fun MusicServiceButton(
	modifier: Modifier = Modifier,
	painter: Painter,
	contentDescription: String? = null,
	musicServiceDialogViewModel: MusicServiceDialogViewModel
) {
	var isDialogOpened by remember { mutableStateOf(false) }
	val isAuthorised by musicServiceDialogViewModel.isAuthorizedState.collectAsState()

	BadgedBox(badge = {
		if (isAuthorised)
			Badge(
				modifier = Modifier
					.offset(x = (-15).dp)
					.size(28.dp),
				containerColor = MaterialTheme.colorScheme.acceptColor,
			) {
				Icon(
					painter = painterResource(id = R.drawable.round_how_to_reg_24),
					contentDescription = "authorized",
					tint = MaterialTheme.colorScheme.onAcceptColor
				)
			}
	}) {
		IconButton(
			onClick = { isDialogOpened = true },
			modifier = modifier
				.shadow(
					elevation = 4.dp,
					shape = RoundedCornerShape(10.dp),
					spotColor = Color.Black
				)
				.background(
					color = MaterialTheme.colorScheme.primaryContainer,
					shape = RoundedCornerShape(10.dp)
				)
				.padding(5.dp)
				.size(70.dp)
		) {
			Image(
				painter = painter,
				contentDescription = contentDescription,
				modifier = Modifier.size(48.dp)
			)
		}
	}

	if (isDialogOpened)
		MusicServiceDialog(
			musicServiceDialogViewModel = musicServiceDialogViewModel,
			formIcon = painter,
		) { state -> isDialogOpened = state }
}