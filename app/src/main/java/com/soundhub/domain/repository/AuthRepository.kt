package com.soundhub.domain.repository

import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.responses.internal.LogoutResponse
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.domain.model.User

interface AuthRepository {
	suspend fun signIn(body: SignInRequestBody): HttpResult<UserPreferences>
	suspend fun signUp(body: RegisterRequestBody): HttpResult<UserPreferences>
	suspend fun logout(
		authorizedUser: User?,
		accessToken: String?
	): HttpResult<LogoutResponse>

	suspend fun refreshToken(body: RefreshTokenRequestBody): HttpResult<UserPreferences>
}