package com.soundhub.ui.post_editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.ui.events.UiEvent
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.data.repository.PostRepository
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import com.soundhub.utils.mappers.PostMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostEditorViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    userCredsStore: UserCredsStore
): ViewModel() {
    private val userCreds = userCredsStore.getCreds()
    val postEditorState: MutableStateFlow<PostEditorState> = MutableStateFlow(PostEditorState())

    init {
        viewModelScope.launch {
            postEditorState.update {
                it.copy(userCreds = userCreds.firstOrNull())
            }
        }
    }

    fun setContent(value: String) = postEditorState.update {
        it.copy(content = value)
    }

    suspend fun loadPost(postId: UUID) {
        val creds: UserPreferences? = userCreds.firstOrNull()

        postRepository.getPostById(
            accessToken = creds?.accessToken,
            postId = postId
        ).onSuccess { response ->
            val post: Post? = response.body
            post?.let {
                val newState: PostEditorState = PostMapper.impl
                    .fromPostToPostEditorState(post)
                    .apply {
                        this.doesPostExist = true
                        this.oldPostState = post
                    }

                postEditorState.update { newState }
            }
        }
    }

    fun setImages(list: List<String>) {
        val doesPostExist: Boolean = postEditorState.value.doesPostExist

        if (doesPostExist) {
            postEditorState.update {
                it.copy(newImages = list)
            }
        }
        else {
            var images: MutableList<String> = postEditorState.value.images.toMutableList()
            if (images.isNotEmpty())
                images += list
            else images = list.toMutableList()

            postEditorState.update {
                it.copy(images = images)
            }
        }
    }

    fun deleteImage(uri: String) = postEditorState.update {
        val doesPostExist: Boolean = postEditorState.value.doesPostExist
        val originalImages: List<String> = postEditorState.value.oldPostState?.images ?: emptyList()
        val imagesToBeDeleted: MutableList<String> = it.imagesToBeDeleted.toMutableList()

        if (doesPostExist && uri in originalImages) {
            imagesToBeDeleted += uri
        }

        it.copy(
            images = it.images.filter { u -> u != uri },
            imagesToBeDeleted = it.imagesToBeDeleted + imagesToBeDeleted
        )
    }

    suspend fun createPost(author: User?) {
        val creds: UserPreferences? = userCreds.firstOrNull()
        var toastText: UiText = UiText.StringResource(R.string.toast_post_created_successfully)
        val post: Post = PostMapper.impl.fromPostEditorStateToPost(postEditorState.value)
            .apply {
                this.author = author
                this.publishDate = LocalDateTime.now()
            }

        postRepository.addPost(
            post = post,
            accessToken = creds?.accessToken
        ).onSuccess {
            with(uiStateDispatcher) {
                sendUiEvent(UiEvent.ShowToast(toastText))
                sendUiEvent(UiEvent.PopBackStack)
            }
        }
        .onFailure { response ->
            response.errorBody.detail?.let { message ->
                toastText = UiText.DynamicString(message)
                uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
            }
        }
    }

    suspend fun updatePost() {
        val creds: UserPreferences? = userCreds.firstOrNull()
        val post: Post = PostMapper.impl.fromPostEditorStateToPost(postEditorState.value)
        var toastText: UiText

        postRepository.updatePost(
            accessToken = creds?.accessToken,
            postId = post.id,
            post = post,
            newImages = postEditorState.value.newImages,
            imagesToBeDeleted = postEditorState.value.imagesToBeDeleted
        )
            .onSuccess {
                toastText = UiText.StringResource(R.string.toast_post_updated_successfully)
                with(uiStateDispatcher) {
                    sendUiEvent(UiEvent.ShowToast(toastText))
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
            .onFailure {
                toastText = UiText.StringResource(R.string.toast_update_post_error)
                uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
            }
    }
}