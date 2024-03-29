package com.soundhub.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.UiEvent
import com.soundhub.ui.states.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class UiStateDispatcher @Inject constructor() : ViewModel() {
    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.asSharedFlow()

    var uiState = MutableStateFlow(UiState())
        private set

    fun clearState() = uiState.update { UiState() }

    fun toggleSearchBarActive() = uiState.update {
        it.copy(isSearchBarActive = !it.isSearchBarActive, searchBarText = "")
    }

    fun setSearchBarActive(value: Boolean) = uiState.update {
        it.copy(isSearchBarActive = value, searchBarText = "")
    }

    fun updateSearchBarText(value: String) = uiState.update {
        it.copy(searchBarText = value)
    }

    fun setGalleryUrls(value: List<String>) = uiState.update {
        it.copy(galleryImageUrls = value)
    }

    fun sendUiEvent(event: UiEvent) = viewModelScope.launch {
        _uiEvent.emit(event)
    }
}