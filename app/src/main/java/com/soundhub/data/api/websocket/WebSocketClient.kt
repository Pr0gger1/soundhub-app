package com.soundhub.data.api.websocket

import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.requests.SendMessageRequest
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.utils.constants.ApiEndpoints.ChatWebSocket.WS_DELETE_MESSAGE_ENDPOINT
import com.soundhub.utils.constants.ApiEndpoints.ChatWebSocket.WS_READ_MESSAGE_ENDPOINT
import com.soundhub.utils.constants.ApiEndpoints.ChatWebSocket.WS_SEND_MESSAGE_ENDPOINT
import com.soundhub.utils.constants.Constants.DELETER_ID_HEADER
import com.soundhub.utils.constants.Constants.DESTINATION_HEADER
import com.soundhub.utils.constants.Constants.DYNAMIC_PARAM_REGEX
import com.soundhub.utils.lib.HttpUtils.Companion.AUTHORIZATION_HEADER
import com.soundhub.utils.lib.HttpUtils.Companion.getBearerToken
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompCommand
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage
import java.util.UUID
import javax.inject.Inject

class WebSocketClient @Inject constructor(
	userCredsStore: UserCredsStore
) {
	private val userCredsFlow: Flow<UserPreferences> = userCredsStore.getCreds()
	private var stompClient: StompClient? = null
	private val gson = Gson()
	private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

	fun connect(url: String) = coroutineScope.launch {
		val stompHeaders = getConnectionStompHeaders()

		stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)
		stompClient?.connect(stompHeaders)
		Log.i("WebSocketClient", "connect[1]: connected successfully")
	}

	private suspend fun getConnectionStompHeaders(): List<StompHeader> {
		val accessToken = userCredsFlow.firstOrNull()?.accessToken
		val bearerToken: String = getBearerToken(accessToken)

		return listOf(StompHeader(AUTHORIZATION_HEADER, bearerToken))
	}

	fun sendMessage(
		messageRequest: SendMessageRequest?,
		onComplete: () -> Unit = {},
		onError: (e: Throwable) -> Unit = {}
	): Disposable? {
		val jsonObject: String = gson.toJson(messageRequest)
		return stompClient
			?.send(WS_SEND_MESSAGE_ENDPOINT, jsonObject)
			?.subscribe({ onComplete() }, {
				Log.e("WebSocketClient", "sendMessage[error]: ${it.stackTraceToString()}")
				onError(it)
			})
	}

	fun readMessage(
		messageId: UUID,
		onComplete: () -> Unit = {},
		onError: (e: Throwable) -> Unit = {}
	): Disposable? {
		val endpoint: String = replaceParam(messageId, WS_READ_MESSAGE_ENDPOINT)
		val stompMessage = StompMessage(
			StompCommand.SEND,
			listOf(StompHeader(DESTINATION_HEADER, endpoint)),
			null
		)

		return stompClient
			?.send(stompMessage)
			?.subscribe(
				{ onComplete() },
				{ e ->
					Log.e("WebSocketClient", "readMessage[error]: ${e.stackTraceToString()}")
					onError(e)
				}
			)
	}

	fun deleteMessage(
		messageId: UUID,
		deleterId: UUID,
		onComplete: () -> Unit = {},
		onError: (e: Throwable) -> Unit = {}
	): Disposable? {
		val endpoint: String = replaceParam(messageId, WS_DELETE_MESSAGE_ENDPOINT)

		val stompMessage = StompMessage(
			StompCommand.SEND,
			listOf(
				StompHeader(DESTINATION_HEADER, endpoint),
				StompHeader(DELETER_ID_HEADER, deleterId.toString())
			),
			null
		)

		return stompClient
			?.send(stompMessage)
			?.subscribe(
				{
					Log.d(
						"WebSocketClient",
						"deleteMessage[1]: message with id $messageId deleted successfully"
					)
					onComplete()
				},
				{
					Log.e("WebSocketClient", "deleteMessage[2]: ${it.stackTraceToString()}")
					onError(it)
				}
			)
	}

	fun subscribe(
		topic: String,
		messageListener: (StompMessage) -> Unit,
		errorListener: (Throwable) -> Unit = {}
	): Disposable? {
		Log.i("WebSocketClient", "subscribed to $topic")
		return stompClient?.topic(topic)?.subscribe(
			{ message -> messageListener(message) },
			{ error ->
				Log.e("WebSocketClient", "subscribe[error]: ${error.stackTraceToString()}")
				errorListener(error)
			}
		)
	}

	fun disconnect() {
		Log.i("WebSocketClient", "Disconnected")
		stompClient?.disconnect()
	}

	private fun replaceParam(id: UUID, endpoint: String): String {
		val regex = Regex(DYNAMIC_PARAM_REGEX)
		return regex.replace(endpoint, id.toString())
	}
}
