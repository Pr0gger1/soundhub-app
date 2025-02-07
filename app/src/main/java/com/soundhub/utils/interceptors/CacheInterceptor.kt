package com.soundhub.utils.interceptors

import android.util.Log
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val response: Response = chain.proceed(chain.request())
		val cacheControl = CacheControl.Builder()
			.maxAge(1, TimeUnit.HOURS)
			.minFresh(15, TimeUnit.MINUTES)
			.maxStale(7, TimeUnit.DAYS)
			.build()

		Log.d("CacheInterceptor", "intercept[1]: cacheControl = $cacheControl")

		return response.newBuilder()
			.header("Cache-Control", cacheControl.toString())
			.build()
	}
}