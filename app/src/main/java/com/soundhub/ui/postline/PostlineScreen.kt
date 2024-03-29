package com.soundhub.ui.postline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.ui.components.CircleLoader
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.postline.components.PostCard

@Composable
fun PostLineScreen(
    modifier: Modifier = Modifier,
    postLineViewModel: PostlineViewModel = hiltViewModel(),
    uiStateDispatcher: UiStateDispatcher = hiltViewModel(),
    navController: NavHostController,
) {
    val postlineUiState by postLineViewModel.postsUiState.collectAsState()

    ContentContainer(contentAlignment = Alignment.Center) {
        if (postlineUiState.isLoading)
            CircleLoader(modifier = Modifier.size(72.dp))
        else if (postlineUiState.posts.isEmpty())
            Text(text = stringResource(id = R.string.empty_postline_screen))
        else {
            LazyColumn(
                modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(items = postlineUiState.posts, key = { it.id }) { post ->
                    PostCard(
                        navController = navController,
                        post = post
                    )
                }
            }
        }
    }
}