package com.soundhub.data.repository.implementations

import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.ChatService
import com.soundhub.data.api.requests.CreateChatRequestBody
import com.soundhub.data.api.responses.ApiStateResponse
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Chat
import com.soundhub.data.repository.ChatRepository
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.HttpUtils
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatService: ChatService
): ChatRepository {
    override suspend fun getAllChatsByUserId(accessToken: String?, userId: UUID): HttpResult<List<Chat>> {
        try {
            val response: Response<List<Chat>> = chatService
                .getAllChatsByUserId(HttpUtils.getBearerToken(accessToken), userId)
            Log.d("ChatRepository", "getAllChatsByCurrentUser[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(status = response.code())

                Log.e("ChatRepository", "getAllChatsByCurrentUser[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }
            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("ChatRepository", "getAllChatsByCurrentUser[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun getChatById(accessToken: String?, chatId: UUID): HttpResult<Chat?> {
        try {
            val response: Response<Chat?> = chatService
                .getChatById(
                    accessToken = HttpUtils.getBearerToken(accessToken),
                    chatId = chatId
            )

            Log.d("ChatRepository", "getChatById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(status = response.code())

                Log.e("ChatRepository", "getChatById[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }
            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("ChatRepository", "getChatById[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun deleteChatById(
        accessToken: String?,
        chatId: UUID
    ): HttpResult<ApiStateResponse> {
        try {
            val response: Response<ApiStateResponse> = chatService
                .deleteChatById(
                    accessToken = HttpUtils.getBearerToken(accessToken),
                    chatId = chatId
            )
            Log.d("ChatRepository", "deleteChatById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(status = response.code())

                Log.e("ChatRepository", "deleteChatById[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }
            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("ChatRepository", "deleteChatById[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun createChat(
        accessToken: String?,
        body: CreateChatRequestBody
    ): HttpResult<Chat> {
        try {
            val response: Response<Chat> = chatService.createChat(
                    accessToken = HttpUtils.getBearerToken(accessToken),
                    body = body
            )
            Log.d("ChatRepository", "createChat[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(status = response.code())

                Log.e("ChatRepository", "createChat[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }
            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("ChatRepository", "createChat[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }
}