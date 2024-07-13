package com.soundhub.ui.viewmodels

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserSettingsStore
import com.soundhub.data.datastore.model.UserSettings
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UiState
import com.soundhub.utils.enums.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UiStateDispatcher @Inject constructor(
    private val userSettingsStore: UserSettingsStore
) : ViewModel() {
    private val userSettings: Flow<UserSettings> = userSettingsStore.getCreds()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: Flow<UiState> = _uiState.asStateFlow()

    private val _receivedMessages = Channel<Message>(Channel.BUFFERED)
    val receivedMessages: Flow<Message> = _receivedMessages.receiveAsFlow()

    private val _readMessages = Channel<Message>(Channel.BUFFERED)
    val readMessages: Flow<Message> = _readMessages.receiveAsFlow()

    private val _deletedMessages = Channel<UUID>(Channel.BUFFERED)
    val deletedMessages: Flow<UUID> = _deletedMessages.receiveAsFlow()

    override fun onCleared() {
        super.onCleared()
        Log.d("UiStateDispatcher", "onCleared: viewmodel was cleared")
    }

    fun clearState() = _uiState.update { UiState() }

    suspend fun sendReceivedMessage(message: Message) {
        if (!message.isRead)
            _receivedMessages.send(message)
    }

    suspend fun sendDeletedMessage(messageId: UUID) = _deletedMessages.send(messageId)

    suspend fun sendReadMessage(message: Message) = _readMessages.send(message)

    fun setCurrentRoute(route: String?) = _uiState.update {
        it.copy(currentRoute = route)
    }

    fun setAuthorizedUser(user: User?) = _uiState.update {
        it.copy(authorizedUser = user)
    }

    // search bar visibility
    fun toggleSearchBarActive() = _uiState.update {
        it.copy(isSearchBarActive = !it.isSearchBarActive, searchBarText = "")
    }

    fun setSearchBarActive(value: Boolean) = _uiState.update {
        it.copy(isSearchBarActive = value, searchBarText = "")
    }

    // search bar content
    fun updateSearchBarText(value: String) = _uiState.update {
        it.copy(searchBarText = value)
    }

    fun getSearchBarText(): String = _uiState.value.searchBarText

    fun setGalleryUrls(value: List<String>) = _uiState.update {
        it.copy(galleryImageUrls = value)
    }

    suspend fun sendUiEvent(event: UiEvent) = _uiEvent.send(event)

    fun setTheme(theme: AppTheme) = viewModelScope.launch {
        var settings = userSettings.firstOrNull()

        if (settings?.appTheme != theme) {
            settings = settings?.copy(appTheme = theme)
            userSettingsStore.updateCreds(settings)
        }
    }

    @Composable
    fun getBooleanThemeValue(): Boolean {
        val settings by userSettings.collectAsState(initial = UserSettings())
        return when(settings.appTheme) {
            AppTheme.DARK -> true
            AppTheme.LIGHT -> false
            AppTheme.AUTO -> isSystemInDarkTheme()
        }
    }
}