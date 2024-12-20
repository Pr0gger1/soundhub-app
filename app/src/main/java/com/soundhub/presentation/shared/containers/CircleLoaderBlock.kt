package com.soundhub.presentation.shared.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.soundhub.presentation.shared.loaders.CircleLoader

@Composable
fun CircleLoaderBlock(isLoading: Boolean = false) {
	if (isLoading)
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center
		) { CircleLoader() }
}