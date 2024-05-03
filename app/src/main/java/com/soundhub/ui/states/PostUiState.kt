package com.soundhub.ui.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Post

data class PostUiState(
    val posts: List<Post> = emptyList(),
    val status: ApiStatus = ApiStatus.LOADING
)