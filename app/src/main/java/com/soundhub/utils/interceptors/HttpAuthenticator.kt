package com.soundhub.utils.interceptors

import android.util.Log
import com.soundhub.Route
import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.services.AuthService
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants.UNAUTHORIZED_USER_ERROR_CODE
import com.soundhub.utils.lib.HttpUtils
import com.soundhub.utils.lib.HttpUtils.Companion.AUTHORIZATION_HEADER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import retrofit2.Response
import javax.inject.Inject
import okhttp3.Response as HttpResponse
import okhttp3.Route as HttpRoute

class HttpAuthenticator @Inject constructor(
	private val userCredsStore: UserCredsStore,
	private val uiStateDispatcher: UiStateDispatcher,
	private val authService: AuthService
) : Authenticator {
	private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
	private val coroutineScope = CoroutineScope(Dispatchers.IO)
	private val refreshMutex = Mutex()
	private var isRefreshing = false
	private var refreshResult: UserPreferences? = null

	private val maxRetryCount = 3
	private var retryCount = 0

	override fun authenticate(route: HttpRoute?, response: HttpResponse): Request? {
		Log.d("HttpAuthenticator", "authenticate[response]: $response")
		val oldCreds: UserPreferences? = runBlocking { userCreds.firstOrNull() }

		if (oldCreds?.refreshToken == null) {
			clearCredsAndNavigateToAuthForm()
			return null
		}

		if (response.code == UNAUTHORIZED_USER_ERROR_CODE && retryCount < maxRetryCount) {
			val newCreds: UserPreferences? = runBlocking { refreshTokenMutex(oldCreds) }
			val bearerToken: String = HttpUtils.getBearerToken(newCreds?.accessToken)
			retryCount++

			if (!response.request.headers.names().contains(AUTHORIZATION_HEADER)) {
				retryCount = 0
				return response.request
					.newBuilder()
					.header(AUTHORIZATION_HEADER, bearerToken)
					.build()
			}
		}

		return null
	}

	private suspend fun refreshTokenMutex(oldCreds: UserPreferences): UserPreferences? {
		return refreshMutex.withLock {
			if (!isRefreshing) {
				isRefreshing = true
				try {
					refreshResult = refreshTokenAndUpdateCreds(oldCreds)
				} finally {
					isRefreshing = false
				}
			}
			refreshResult
		}
	}

	private suspend fun refreshTokenAndUpdateCreds(oldCreds: UserPreferences): UserPreferences? {
		val requestBody = RefreshTokenRequestBody(oldCreds.refreshToken)
		val refreshTokenResponse: Response<UserPreferences> = authService.refreshToken(requestBody)
		Log.d("HttpAuthenticator", "refreshToken[1]: response = $refreshTokenResponse")

		if (!refreshTokenResponse.isSuccessful) {
			clearCredsAndNavigateToAuthForm()
			return null
		}

		val newCreds: UserPreferences? = refreshTokenResponse.body()
		Log.d("HttpAuthenticator", "refreshToken[2]: creds = $newCreds")
		userCredsStore.updateCreds(newCreds)
		uiStateDispatcher.sendUiEvent(UiEvent.UpdateUserInstance)

		return newCreds
	}

	private fun clearCredsAndNavigateToAuthForm() = coroutineScope.launch {
		val route = Route.Authentication
		uiStateDispatcher.sendUiEvent(UiEvent.Navigate(route))
		userCredsStore.clear()
	}
}
