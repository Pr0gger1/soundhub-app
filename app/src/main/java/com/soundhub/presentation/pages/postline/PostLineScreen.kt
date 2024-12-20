package com.soundhub.presentation.pages.postline

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.User
import com.soundhub.domain.states.PostLineUiState
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.shared.containers.ContentContainer
import com.soundhub.presentation.shared.containers.FetchStatusContainer
import com.soundhub.presentation.shared.post_card.PostCard
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
fun PostLineScreen(
	modifier: Modifier = Modifier,
	postLineViewModel: PostLineViewModel = hiltViewModel(),
	uiStateDispatcher: UiStateDispatcher,
	navController: NavHostController,
) {
	val context: Context = LocalContext.current
	val postLineUiState by postLineViewModel.postLineUiState.collectAsState(initial = PostLineUiState())
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val authorizedUser: User? = uiState.authorizedUser

	val posts = postLineUiState.posts
	val fetchStatus: ApiStatus = postLineUiState.status

	var messageScreenText: String by rememberSaveable { mutableStateOf("") }

	LaunchedEffect(key1 = authorizedUser) {
		postLineViewModel.loadPosts()
	}

	LaunchedEffect(key1 = posts) {
		if (posts.isEmpty())
			messageScreenText = context.getString(R.string.empty_postline_screen)
	}

	ContentContainer(
		contentAlignment = Alignment.Center,
		modifier = Modifier.padding(top = 10.dp)
	) {
		FetchStatusContainer(status = fetchStatus) {
			if (posts.isEmpty())
				Text(
					text = messageScreenText,
					fontSize = 20.sp,
					fontWeight = FontWeight.Bold,
					textAlign = TextAlign.Center
				)
			else LazyColumn(
				modifier.fillMaxSize(),
				verticalArrangement = Arrangement.spacedBy(20.dp)
			) {
				items(items = posts, key = { it.id }) { post ->
					PostCard(
						navController = navController,
						post = post,
						uiStateDispatcher = uiStateDispatcher,
						onDeletePost = { postLineViewModel::deletePostById },
						onLikePost = { postLineViewModel::togglePostLikeAndUpdatePostList }
					)
				}
			}
		}
	}
}