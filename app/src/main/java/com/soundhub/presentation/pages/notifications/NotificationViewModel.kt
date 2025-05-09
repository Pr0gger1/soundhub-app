package com.soundhub.presentation.pages.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.domain.events.UiEvent
import com.soundhub.domain.model.Invite
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.InviteRepository
import com.soundhub.domain.states.NotificationUiState
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.lib.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
	private val inviteRepository: InviteRepository,
	private val uiStateDispatcher: UiStateDispatcher,
	private val userDao: UserDao,
	userCredsStore: UserCredsStore
) : ViewModel() {
	private val userCreds = userCredsStore.getCreds()

	private val _notificationUiState = MutableStateFlow(NotificationUiState())
	val notificationUiState = _notificationUiState.asStateFlow()

	init {
		loadInvitesInitial()
	}

	private fun loadInvitesInitial() = viewModelScope.launch {
		val authorizedUser = userDao.getCurrentUser()
		val creds = userCreds.firstOrNull()

		if (creds?.accessToken != null && authorizedUser != null)
			loadInvites()
	}

	fun loadInvites() = viewModelScope.launch(Dispatchers.IO) {
		_notificationUiState.update { it.copy(status = ApiStatus.LOADING) }
		inviteRepository.getAllInvites()
			.onSuccessWithContext { response ->
				val invites: List<Invite> = response.body.orEmpty()
				_notificationUiState.update {
					it.copy(
						notifications = invites,
						status = ApiStatus.SUCCESS
					)
				}
			}
			.onFailureWithContext { error ->
				val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
				uiStateDispatcher.sendUiEvent(errorEvent)
				_notificationUiState.update { it.copy(status = ApiStatus.ERROR) }
			}
	}

	fun acceptInvite(invite: Invite) = viewModelScope.launch(Dispatchers.IO) {
		val toastText = UiText.StringResource(R.string.toast_invite_accepted_successfully)
		val uiEvent = UiEvent.ShowToast(toastText)

		inviteRepository.acceptInvite(invite.id)
			.onSuccess {
				deleteNotificationById(invite.id)
				val authorizedUser: User? = userDao.getCurrentUser()

				updateAuthorizedUser(authorizedUser, invite.sender)
			}
			.onFailure { error ->
				val errorEvent: UiEvent = UiEvent.Error(
					error.errorBody,
					error.throwable,
					R.string.toast_accept_invite_error
				)
				uiStateDispatcher.sendUiEvent(errorEvent)
			}
			.finally {
				uiStateDispatcher.sendUiEvent(uiEvent)
			}
	}

	private suspend fun updateAuthorizedUser(user: User?, inviteSender: User) = user?.let {
		with(it) {
			friends += inviteSender
			uiStateDispatcher.setAuthorizedUser(this)
			userDao.saveOrReplaceUser(this)
		}
	}

	fun rejectInvite(invite: Invite) = viewModelScope.launch(Dispatchers.IO) {
		val toastText = UiText.StringResource(R.string.toast_invite_rejected_successfully)
		val uiEvent = UiEvent.ShowToast(toastText)

		inviteRepository.rejectInvite(invite.id)
			.onSuccessWithContext {
				deleteNotificationById(invite.id)
			}
			.onFailureWithContext { error ->
				val errorEvent: UiEvent = UiEvent.Error(
					error.errorBody,
					error.throwable,
					R.string.toast_reject_invite_error
				)
				uiStateDispatcher.sendUiEvent(errorEvent)
			}
			.finallyWithContext { uiStateDispatcher.sendUiEvent(uiEvent) }
	}

	private fun deleteNotificationById(notificationId: UUID) = _notificationUiState.update {
		val notifications = it.notifications.filter { n -> n.id != notificationId }
		it.copy(notifications = notifications)
	}
}