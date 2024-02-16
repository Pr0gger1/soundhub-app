package com.soundhub.ui.components.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ContentContainer(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit = {}
) {
    Box(
        contentAlignment = contentAlignment,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) { content() }
}