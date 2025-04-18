package com.soundhub.di

import android.content.Context
import com.soundhub.data.api.services.AuthService
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants.AUTHORIZED_HTTP_CLIENT_WITH_CACHE
import com.soundhub.utils.constants.Constants.CONNECTION_TIMEOUT
import com.soundhub.utils.constants.Constants.SIMPLE_HTTP_CLIENT
import com.soundhub.utils.constants.Constants.UNATHORIZED_HTTP_CLIENT_WITH_CACHE
import com.soundhub.utils.extensions.createCache
import com.soundhub.utils.interceptors.AuthInterceptor
import com.soundhub.utils.interceptors.CacheInterceptor
import com.soundhub.utils.interceptors.HttpAuthenticator
import com.soundhub.utils.interceptors.OfflineCacheInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {
	@Provides
	@Singleton
	fun providesAuthInterceptor(userCredsStore: UserCredsStore): AuthInterceptor {
		return AuthInterceptor(userCredsStore)
	}

	@Provides
	@Singleton
	fun providesLoggingInterceptor(): HttpLoggingInterceptor {
		return HttpLoggingInterceptor().apply {
			level = HttpLoggingInterceptor.Level.BODY
		}
	}

	@Provides
	@Singleton
	@Named(AUTHORIZED_HTTP_CLIENT_WITH_CACHE)
	fun providesOkHttpClient(
		@ApplicationContext context: Context,
		authInterceptor: AuthInterceptor,
		loggingInterceptor: HttpLoggingInterceptor,
		userCredsStore: UserCredsStore,
		uiStateDispatcher: UiStateDispatcher,
		authService: AuthService
	): OkHttpClient {
		val cache = Cache.createCache(context = context)
		val authenticator = HttpAuthenticator(
			userCredsStore,
			uiStateDispatcher,
			authService
		)

		val offlineCacheInterceptor = OfflineCacheInterceptor()
		val networkInterceptor = CacheInterceptor()

		return OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.addInterceptor(authInterceptor)
			.addInterceptor(offlineCacheInterceptor)
			.addNetworkInterceptor(networkInterceptor)
			.authenticator(authenticator)
			.followRedirects(false)
			.followSslRedirects(false)
			.retryOnConnectionFailure(true)
			.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
			.cache(cache)
			.build()
	}

	@Provides
	@Singleton
	@Named(UNATHORIZED_HTTP_CLIENT_WITH_CACHE)
	fun providesUnauthorizedOkHttpClient(
		@ApplicationContext context: Context,
		loggingInterceptor: HttpLoggingInterceptor
	): OkHttpClient {
		val cache = Cache.createCache(context = context)

		val offlineCacheInterceptor = OfflineCacheInterceptor()
		val networkInterceptor = CacheInterceptor()

		return OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.addInterceptor(offlineCacheInterceptor)
			.addNetworkInterceptor(networkInterceptor)
			.followRedirects(false)
			.followSslRedirects(false)
			.retryOnConnectionFailure(true)
			.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
			.cache(cache)
			.build()
	}

	@Singleton
	@Provides
	@Named(SIMPLE_HTTP_CLIENT)
	fun providesSimpleOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
		return OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.followRedirects(false)
			.followSslRedirects(false)
			.retryOnConnectionFailure(true)
			.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
			.build()
	}
}