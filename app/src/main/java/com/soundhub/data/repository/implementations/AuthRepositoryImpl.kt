package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.R
import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.responses.LogoutResponse
import com.soundhub.data.api.services.AuthService
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.model.User
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.UserRepository
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.enums.ContentTypes
import com.soundhub.utils.lib.HttpUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
	private val authService: AuthService,
	private val userRepository: UserRepository,
	private val context: Context,
	private val gson: Gson
) : AuthRepository {
	override suspend fun signIn(body: SignInRequestBody): HttpResult<UserPreferences?> {
		try {
			val signInResponse: Response<UserPreferences> = authService.signIn(body)
			Log.d("AuthRepository", "signIn[1]: $signInResponse")

			if (!signInResponse.isSuccessful) {
				val errorBody: ErrorResponse = gson
					.fromJson(signInResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
					?: ErrorResponse(
						status = signInResponse.code(),
						detail = context.getString(R.string.toast_authorization_error)
					)

				Log.e("AuthRepository", "signIn[2]: $errorBody")
				return HttpResult.Error(errorBody = errorBody)
			}

			return HttpResult.Success(body = signInResponse.body())
		} catch (e: Exception) {
			Log.e("AuthRepository", "signIn[3]: ${e.stackTraceToString()}")
			return HttpResult.Error(
				errorBody = ErrorResponse(detail = e.localizedMessage),
				throwable = e
			)
		}

	}

	override suspend fun signUp(body: RegisterRequestBody): HttpResult<UserPreferences?> {
		try {
			val avatarFormData: MultipartBody.Part? = HttpUtils
				.prepareMediaFormData(body.avatarUrl, context)

			val requestBody: RequestBody = gson.toJson(body)
				.toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

			val signUpResponse = authService.signUp(requestBody, avatarFormData)
			Log.d("AuthRepository", "signUp[1]: $signUpResponse")

			if (!signUpResponse.isSuccessful) {
				val errorBody: ErrorResponse = gson
					.fromJson(signUpResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
					?: ErrorResponse(
						detail = context.getString(R.string.toast_register_error),
						status = signUpResponse.code()
					)

				Log.e("AuthRepository", "signUp[2]: $errorBody")
				return HttpResult.Error(errorBody = errorBody)
			}

			return HttpResult.Success(body = signUpResponse.body())
		} catch (e: Exception) {
			Log.e("AuthRepository", "signUp[3]: ${e.stackTraceToString()}")
			return HttpResult.Error(
				errorBody = ErrorResponse(e.localizedMessage),
				throwable = e
			)
		}
	}

	override suspend fun logout(
		authorizedUser: User?,
		accessToken: String?
	): HttpResult<LogoutResponse> {
		try {
			val logoutResponse: Response<LogoutResponse> = authService.logout(
				HttpUtils.getBearerToken(accessToken)
			)

			Log.d("AuthRepository", "logout[1]: $logoutResponse")

			if (!logoutResponse.isSuccessful) {
				val errorBody: ErrorResponse = gson
					.fromJson(logoutResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
					?: ErrorResponse(
						status = logoutResponse.code(),
						detail = context.getString(R.string.toast_logout_error)
					)

				Log.e("AuthRepository", "logout[2]: $errorBody")
				return HttpResult.Error(errorBody = errorBody)
			}

			if (authorizedUser?.isOnline == true)
				userRepository.toggleUserOnline()

			return HttpResult.Success(body = logoutResponse.body())
		} catch (e: Exception) {
			Log.e("AuthRepository", "logout[3]: ${e.stackTraceToString()}")
			return HttpResult.Error(
				errorBody = ErrorResponse(detail = e.localizedMessage),
				throwable = e
			)
		}
	}

	override suspend fun refreshToken(body: RefreshTokenRequestBody): HttpResult<UserPreferences?> {
		try {
			val response: Response<UserPreferences> = authService.refreshToken(body)
			Log.d("AuthRepository", "refreshToken[1]: $response")

			if (!response.isSuccessful) {
				val errorBody: ErrorResponse = gson
					.fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
					?: ErrorResponse(
						status = response.code(),
						detail = context.getString(R.string.toast_fetch_user_data_error)
					)

				Log.e("AuthRepository", "refreshToken[2]: $errorBody")
				return HttpResult.Error(errorBody = errorBody)
			}

			return HttpResult.Success(body = response.body())
		} catch (e: Exception) {
			Log.e("AuthRepository", "refreshToken[3]: ${e.stackTraceToString()}")
			return HttpResult.Error(
				errorBody = ErrorResponse(detail = e.localizedMessage),
				throwable = e
			)
		}
	}
}