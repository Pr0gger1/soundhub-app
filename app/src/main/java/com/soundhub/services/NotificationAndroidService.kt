package com.soundhub.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NotificationAndroidService : Service() {
	override fun onBind(intent: Intent?): IBinder? {
		TODO("Not yet implemented")
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		return super.onStartCommand(intent, flags, startId)
	}

}