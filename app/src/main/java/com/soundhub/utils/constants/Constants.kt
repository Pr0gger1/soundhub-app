package com.soundhub.utils.constants

import com.soundhub.BuildConfig
import com.soundhub.Route

object Constants {
	//region date constants
	const val DATE_FORMAT = "yyyy-MM-dd"
	const val LOCAL_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
	const val FALLBACK_LOCAL_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm"

	//endregion

	//region numeric constants
	const val PASSWORD_MIN_LENGTH = 8

	// search debounce in milliseconds
	const val SEARCH_DEBOUNCE_VALUE = 300L

	// online status
	// after destroy: 1 minute
	const val SET_OFFLINE_DELAY_ON_DESTROY = 60 * 1000L

	// after stop: 5 minutes
	const val SET_OFFLINE_DELAY_ON_STOP = 5 * 60 * 1000L

	// highlight message timeout after click
	const val HIGHLIGHT_MESSAGE_TIMEOUT = 1500

	//endregion

	//region regular expressions
	const val EMAIL_REGEX: String = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
	const val DYNAMIC_PARAM_REGEX: String = """\{[^}]*\}"""
	const val URL_WITH_PARAMS_REGEX: String = "/\\S+\\?\\w+=\\w+"

	// endregion

	//region datastore identifiers
	const val DATASTORE_USER_SETTINGS = "user_settings"
	const val DATASTORE_APP_THEME = "settings_app_theme"

	const val DATASTORE_USER_CREDS = "user_creds"
	const val DATASTORE_ACCESS_TOKEN = "user_access_token"
	const val DATASTORE_REFRESH_TOKEN = "user_refresh_token"

	// room db
	const val ROOM_DB_NAME = "soundhub_db"

	//endregion

	//region route list constants
	val ROUTES_WITH_CUSTOM_TOP_APP_BAR: List<String> = listOf(
		Route.PostLine.route,
		Route.Music.route,
		Route.Messenger.route,
	)

	val ROUTES_WITHOUT_TOP_APP_BAR: List<String> = listOf(
		Route.Authentication.withNavArg,
		Route.Authentication.route,
		Route.Profile.route,
		Route.EditFavoriteArtists.route,
		Route.EditFavoriteGenres.route,
		Route.Music.LastFmProfile.route
	)

	val ROUTES_WITH_INNER_TOP_APP_BAR: List<String> = listOf(
		Route.EditUserData.route,
		Route.Messenger.Chat.route
	)

	val ROUTES_WITH_BOTTOM_BAR: List<String> = listOf(
		Route.Profile.route,
		Route.PostLine.route,
		Route.Music.route,
		Route.Messenger.route,
		Route.Music.FavoriteArtists.route,
		Route.Music.FavoriteGenres.route,
		Route.Music.LastFmProfile.route
	)

	val ROUTES_WITH_SEARCH_BAR: List<String> = listOf(
		Route.Music.route,
		Route.Messenger.route,
		Route.Profile.Friends.route
	)

	//endregion

	//region nav arguments
	const val POST_REGISTER_NAV_ARG = "postAuthId"
	const val PROFILE_NAV_ARG = "userId"
	const val CHAT_NAV_ARG = "chatId"
	const val GALLERY_INITIAL_PAGE_NAV_ARG = "imageIndex"
	const val POST_EDITOR_NAV_ARG = "postId"

	val ALL_NAV_ARGS: List<String> = listOf(
		POST_REGISTER_NAV_ARG,
		PROFILE_NAV_ARG,
		CHAT_NAV_ARG,
		GALLERY_INITIAL_PAGE_NAV_ARG,
		POST_EDITOR_NAV_ARG
	)

	//endregion

	//region routes
	// authentication routes
	const val AUTHENTICATION_ROUTE = "authentication"
	const val CHOOSE_GENRES_ROUTE = "$AUTHENTICATION_ROUTE/chooseGenres"
	const val CHOOSE_ARTISTS_ROUTE = "$AUTHENTICATION_ROUTE/chooseArtists"
	const val FILL_DATA_REGISTRATION_ROUTE = "$AUTHENTICATION_ROUTE/fillDataRegister"

	const val PROFILE_ROUTE = "profile/{$PROFILE_NAV_ARG}"
	const val FRIENDS_ROUTE = "${PROFILE_ROUTE}/friends"
	const val POSTLINE_ROUTE = "postline"
	const val MUSIC_ROUTE = "music"

	// messenger routes
	const val MESSENGER_ROUTE = "messenger"
	const val MESSENGER_CHAT_ROUTE = "$MESSENGER_ROUTE/chat/{$CHAT_NAV_ARG}"

	const val SETTINGS_ROUTE = "settings"
	const val NOTIFICATIONS_ROUTE = "notifications"
	const val EDIT_USER_DATA_ROUTE = "user-editor"
	const val EDIT_FAV_GENRES_ROUTE = "fav-genres-editor"
	const val EDIT_FAV_ARTISTS_ROUTE = "fav-artists-editor"
	const val POST_EDITOR_ROUTE = "post-editor/{$POST_EDITOR_NAV_ARG}"
	const val GALLERY_ROUTE = "gallery"

	// music page
	const val MUSIC_NEW_OF_THE_WEEK = "music-new-of-the-week"
	const val MUSIC_NEW_OF_THE_MONTH = "music-new-of-the-month"
	const val MUSIC_RECOMMENDATIONS = "music-recommend-music"
	const val MUSIC_FAVORITE_GENRES = "music-favorite-genres"
	const val MUSIC_FAVORITE_ARTISTS = "music-favorite-artists"
	const val MUSIC_PLAYLISTS = "music-playlists"
	const val LASTFM_PROFILE = "lastfm-profile"

	//endregion

	//region response error body type
	const val METHOD_NOT_IMPLEMENTED_ERROR = "This method should be implemented"

	//endregion

	//region named hilt injections
	const val COUNTRIES_API_RETROFIT = "countries_api"
	const val AUTHORIZED_SOUNDHUB_API_RETROFIT = "authorized_soundhub_api"
	const val UNAUTHORIZED_SOUNDHUB_API_RETROFIT = "soundhub_api"
	const val LAST_FM_API_RETROFIT = "lastfm_api"
	const val MUSIC_API_RETROFIT = "music_api"
	const val SOUNDHUB_AUTH_SERVICE_RETROFIT = "auth_service_api"

	const val UNATHORIZED_HTTP_CLIENT_WITH_CACHE = "unauthorized_http_client"
	const val SIMPLE_HTTP_CLIENT = "simple_http_client"
	const val AUTHORIZED_HTTP_CLIENT_WITH_CACHE = "authorized_http_client"

	// endregion

	//region network constants
	const val COUNTRIES_API = "https://restcountries.com/v3.1/"
	const val SOUNDHUB_API = "http://${BuildConfig.SOUNDHUB_API_HOSTNAME}/api/v1/"
	const val LAST_FM_API = "https://ws.audioscrobbler.com/2.0/"
	const val DISCOGS_API = "https://api.discogs.com/"
	const val DISCOGS_AUTHORIZATION =
		"Discogs key=${BuildConfig.DISCOGS_KEY}, secret=${BuildConfig.DISCOGS_SECRET}"

	//endregion

	//region websocket constants
	const val SOUNDHUB_WEBSOCKET = "ws://${BuildConfig.SOUNDHUB_API_HOSTNAME}/ws"
	const val DELETER_ID_HEADER = "DeleterId"
	const val DESTINATION_HEADER = "destination"

	const val UNAUTHORIZED_USER_ERROR_CODE = 401
	const val INVALID_ACCESS_TOKEN_MESSAGE = "Invalid access token"
	const val NOT_FOUND_ERROR_CODE = 404
	const val CONNECTION_TIMEOUT = 10L

	const val CACHE_SIZE: Long = 10 * 1024 * 1024

	//endregion

	//region Paging constants
	const val DEFAULT_MESSAGE_PAGE_SIZE = 100
	const val DEFAULT_MESSAGE_PAGE = 1
	const val DEFAULT_ARTIST_PAGE_SIZE = 20

	//endregion

	//region other constants
	const val MAX_MESSAGE_PREVIEW_LENGTH = 20

	//endregion
}