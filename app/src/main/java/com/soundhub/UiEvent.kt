package com.soundhub

sealed class UiEvent {
    object PopBackStack: UiEvent()
    data class Navigate(val route: Route): UiEvent() {
        override fun toString(): String {
            return "Route{\n" +
                    "route: ${route.route}\n}"
        }
    }

    data class ShowToast(
        val message: String,
        val action: String? = null
    ): UiEvent()

    object SearchButtonClick: UiEvent()
    data class Loading(val isLoading: Boolean = false): UiEvent()
    data class Error(val message: String = ""): UiEvent()
}