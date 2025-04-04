package com.soundhub.data.enums

enum class ApiStatus {
	SUCCESS, ERROR, LOADING, NOT_LAUNCHED;

	fun isLoading() = this == LOADING
	fun isError() = this == ERROR
	fun isSuccess() = this == SUCCESS
}