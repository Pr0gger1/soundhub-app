package com.soundhub.ui.pages.profile.components.sections.wall

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.model.Post
import com.soundhub.data.states.ProfileUiState
import com.soundhub.ui.pages.profile.ProfileViewModel
import com.soundhub.ui.pages.profile.components.SectionLabel
import com.soundhub.ui.shared.containers.FetchStatusContainer
import com.soundhub.ui.shared.post_card.PostCard
import com.soundhub.ui.viewmodels.PostViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
internal fun UserWall(
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher,
	profileViewModel: ProfileViewModel,
	postViewModel: PostViewModel = hiltViewModel(),
) {
	val profileUiState: ProfileUiState by profileViewModel.profileUiState.collectAsState()
	val posts: List<Post> = profileUiState.userPosts
	val fetchStatus = profileUiState.postStatus

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(vertical = 10.dp),
		verticalArrangement = Arrangement.spacedBy(10.dp),
	) {
		SectionLabel(
			text = stringResource(id = R.string.profile_screen_user_posts),
			labelIcon = painterResource(id = R.drawable.round_sticky_note_2_24),
			iconTint = Color(0xFFFFC107),
		)

		FetchStatusContainer(
			status = fetchStatus,
			modifier = Modifier.padding(top = 20.dp)
		) {
			posts.forEach { post ->
				PostCard(
					post = post,
					navController = navController,
					uiStateDispatcher = uiStateDispatcher,
					postViewModel = postViewModel,
					onDeletePost = { id -> profileViewModel.deletePostById(id) },
					onLikePost = { id -> profileViewModel.togglePostLikeAndUpdatePostList(id) }
				)
			}
		}
	}
}