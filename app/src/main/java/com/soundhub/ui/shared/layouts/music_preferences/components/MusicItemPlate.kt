package com.soundhub.ui.shared.layouts.music_preferences.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.soundhub.R

@Composable
fun MusicItemPlate(
	modifier: Modifier = Modifier,
	clickable: Boolean = true,
	caption: String,
	thumbnailUrl: String?,
	onClick: (Boolean) -> Unit = {},
	isChosen: Boolean = false,
	width: Dp = 72.dp,
	height: Dp = 72.dp
) {
	val tertiaryColor = MaterialTheme.colorScheme.tertiary
	var isItemChosen by rememberSaveable { mutableStateOf(isChosen) }
	var itemBoxModifier: Modifier = Modifier
		.width(width)
		.height(height)

	if (isItemChosen && clickable)
		itemBoxModifier = itemBoxModifier
			.border(
				width = 8.dp,
				color = tertiaryColor,
				shape = RoundedCornerShape(16.dp)
			)

	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		ElevatedCard(
			modifier = itemBoxModifier,
			shape = RoundedCornerShape(16.dp),
			onClick = {
				if (clickable) {
					isItemChosen = !isItemChosen
					onClick(isItemChosen)
				}
			}
		) {
			SubcomposeAsyncImage(
				model = ImageRequest.Builder(LocalContext.current)
					.addHeader("mode", "no-cors")
					.data(thumbnailUrl)
					.crossfade(true)
					.build(),
				contentDescription = caption,
				contentScale = ContentScale.Crop,
			) {
				val state = painter.state
				if (state is AsyncImagePainter.State.Error || state is AsyncImagePainter.State.Loading)
					SubcomposeAsyncImageContent(
						modifier = Modifier.fillMaxSize(),
						contentScale = ContentScale.Crop,
						painter = painterResource(id = R.drawable.musical_note)
					)
			}
//            GlideImage(
//                model = HttpUtils.prepareGlideUrlDiscogs(thumbnailUrl) ?: "kkk",
//                failure = placeholder(painterResource(id = R.drawable.musical_note)),
//                contentScale = ContentScale.Crop,
//                contentDescription = thumbnailUrl,
//                modifier = Modifier.fillMaxSize()
//            )
		}
		Text(
			text = caption,
			style = TextStyle(
				fontSize = 15.sp,
				textAlign = TextAlign.Center,
				color = MaterialTheme.colorScheme.onSecondaryContainer,
			)
		)
	}
}

@Preview
@Composable
private fun ItemPlatePreview() {
	MusicItemPlate(caption = "Rap", thumbnailUrl = "")
}