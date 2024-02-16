package com.soundhub.utils

import com.soundhub.Route

object Constants {
    const val PASSWORD_MIN_LENGTH: Int = 6
    const val EMAIL_MASK: String = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    const val DYNAMIC_PART_ROUTE_MASK: String = """/\{[^}]*\}"""

    const val DB_USERS: String = "users"

    // datastore identifiers
    const val DATASTORE_USER_CREDS = "user_creds"
    const val DATASTORE_USER_FIRST_NAME = "user_first_name"
    const val DATASTORE_USER_LASTNAME = "user_last_name"
    const val DATASTORE_USER_ID = "user_id"
    const val DATASTORE_USER_COUNTRY = "user_country"
    const val DATASTORE_USER_CITY = "user_city"
    const val DATASTORE_USER_EMAIL = "user_email"
    const val DATASTORE_USER_DESCRIPTION = "user_description"
    /* TODO: make datastore saving logic for user languages */
    const val DATASTORE_USER_LANGUAGES = "user_languages"
    const val DATASTORE_SESSION_TOKEN = "user_session_token"

    const val LOG_CURRENT_EVENT_TAG = "current_event"
    const val LOG_USER_CREDS_TAG = "user_creds"
    const val LOG_REGISTER_STATE = "registration_state"
    const val LOG_CURRENT_ROUTE = "current_route"

    const val DATE_FORMAT = "yyyy-MM-dd"

    const val POST_REGISTER_NAV_ARG = "postAuthId"
    const val PROFILE_NAV_ARG = "userId"
    const val CHAT_NAV_ARG = "chatId"
    const val GALLERY_NAV_ARG = "imageIndex"

    val ROUTES_WITH_CUSTOM_TOP_APP_BAR: List<String> = listOf(
        Route.Postline.route,
        Route.Music.route,
        Route.Messenger.route
    )

    val ROUTES_WITHOUT_TOP_APP_BAR: List<String> = listOf(
        Route.Authentication.route,
        Route.Authentication.ChooseGenres.route,
        Route.Authentication.ChooseArtists.route,
        Route.Authentication.FillUserData.route,
        Route.Profile.route
    )

    val ROUTES_WITH_BOTTOM_BAR: List<String> = listOf(
        Route.Profile.route,
        Route.Postline.route,
        Route.Music.route,
        Route.Messenger.route
    )
}