package com.soundhub.presentation.shared.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
	modifier: Modifier = Modifier,
	sheetContent: @Composable () -> Unit,
	content: @Composable () -> Unit = {},
	scaffoldState: BottomSheetScaffoldState
) {
	val keyboardController = LocalSoftwareKeyboardController.current

	if (scaffoldState.bottomSheetState.currentValue != SheetValue.Expanded)
		keyboardController?.hide()

	BottomSheetScaffold(
		modifier = modifier,
		scaffoldState = scaffoldState,
		sheetPeekHeight = 0.dp,
		sheetContent = { sheetContent() },
	) { content() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun BottomSheetPreview() {
	val coroutineScope = rememberCoroutineScope()
	val scaffoldState = rememberBottomSheetScaffoldState(
		SheetState(
			skipPartiallyExpanded = false,
			density = LocalDensity.current,
			initialValue = SheetValue.Expanded
		)
	)

	BottomSheet(sheetContent = {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center
		) {
			Button(
				onClick = { coroutineScope.launch { scaffoldState.bottomSheetState.expand() } }
			) { Text("Click me") }
		}
	}, scaffoldState = scaffoldState)
}
