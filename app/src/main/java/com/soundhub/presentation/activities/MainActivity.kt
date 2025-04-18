package com.soundhub.presentation.activities

import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soundhub.R
import com.soundhub.domain.events.UiEvent
import com.soundhub.domain.services.MessengerAndroidService
import com.soundhub.presentation.layout.RootLayout
import com.soundhub.presentation.navigation.NavigationViewModel
import com.soundhub.presentation.pages.authentication.AuthenticationViewModel
import com.soundhub.presentation.pages.messenger.MessengerViewModel
import com.soundhub.presentation.pages.music.viewmodels.MusicViewModel
import com.soundhub.presentation.pages.notifications.NotificationViewModel
import com.soundhub.presentation.pages.registration.RegistrationViewModel
import com.soundhub.presentation.theme.SoundHubTheme
import com.soundhub.presentation.viewmodels.SplashScreenViewModel
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.extensions.activity.requestNotificationPermissions
import com.soundhub.utils.extensions.context_wrapper.registerReceiverExtended
import com.soundhub.utils.extensions.context_wrapper.startAndroidService
import com.soundhub.utils.extensions.context_wrapper.stopAndroidService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private val registrationViewModel: RegistrationViewModel by viewModels()
	private val splashScreenViewModel: SplashScreenViewModel by viewModels()
	private val authViewModel: AuthenticationViewModel by viewModels()
	private val messengerViewModel: MessengerViewModel by viewModels()
	private val uiStateDispatcher: UiStateDispatcher by viewModels()
	private val notificationViewModel: NotificationViewModel by viewModels()
	private val navigationViewModel: NavigationViewModel by viewModels()
	private val musicViewModel: MusicViewModel by viewModels()

	private lateinit var navController: NavHostController
	private var isMessengerConnected = false
	private lateinit var messengerAndroidService: MessengerAndroidService

	private val messengerConnection = object : ServiceConnection {
		override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
			messengerAndroidService = (service as MessengerAndroidService.LocalBinder).getService()
			isMessengerConnected = true
		}

		override fun onServiceDisconnected(name: ComponentName?) {
			isMessengerConnected = false
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		runActionsOnCreateActivity()
		installSplashScreen().apply {
			setKeepOnScreenCondition { splashScreenViewModel.isLoading.value }
		}

		setContent {
			SoundHubTheme(
				dynamicColor = false,
				darkTheme = uiStateDispatcher.isDarkTheme()
			) {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.surface
				) {
					navController = rememberNavController()
					NavigationListener()
					RootLayout(
						navController = navController,
						authViewModel = authViewModel,
						uiStateDispatcher = uiStateDispatcher,
						messengerViewModel = messengerViewModel,
						registrationViewModel = registrationViewModel,
						notificationViewModel = notificationViewModel,
						musicViewModel = musicViewModel
					)
				}
			}
		}
	}

	override fun onStart() {
		super.onStart()
		bindMessengerService()
	}

	override fun onResume() {
		super.onResume()
		authViewModel.updateUserOnlineStatusDelayed(true)
		registerReceivers()
	}

	override fun onPause() {
		super.onPause()
		unregisterReceivers()
	}

	override fun onStop() {
		super.onStop()
		authViewModel.updateUserOnlineStatusDelayed(false, Constants.SET_OFFLINE_DELAY_ON_STOP)
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.d("MainActivity", "onDestroy: user has closed the app")

		unbindMessengerService()
		stopAndroidService(MessengerAndroidService::class.java)
		uiStateDispatcher.clearState()
		authViewModel.updateUserOnlineStatusDelayed(false, Constants.SET_OFFLINE_DELAY_ON_DESTROY)
	}

	private fun bindMessengerService() {
		Intent(this, MessengerAndroidService::class.java).also { intent ->
			bindService(intent, messengerConnection, BIND_AUTO_CREATE)
		}
	}

	private fun unbindMessengerService() {
		if (isMessengerConnected) {
			unbindService(messengerConnection)
			isMessengerConnected = false
		}
	}

	private fun registerReceivers() {
		messengerViewModel.getMessageReceiversWithIntentFilters().forEach { (receiver, action) ->
			registerReceiverExtended(receiver, IntentFilter(action))
		}
	}

	private fun unregisterReceivers() {
		messengerViewModel.getMessageReceivers().forEach { unregisterReceiver(it) }
	}

	@Composable
	private fun NavigationListener() {
		val coroutineScope = rememberCoroutineScope()
		navController.addOnDestinationChangedListener { controller, destination, _ ->
			coroutineScope.launch {
				navigationViewModel.onNavDestinationChangedListener(controller, destination)
			}
		}
	}

	private fun runActionsOnCreateActivity() {
		requestNotificationPermissions()
		startAndroidService(MessengerAndroidService::class.java)
		lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				observeUiEvents()
			}
		}
	}

	private suspend fun observeUiEvents() {
		uiStateDispatcher.uiEvent.collect { event -> handleUiEvent(event, navController) }
	}

	private fun handleUiEvent(event: UiEvent, navController: NavHostController) {
		Log.d("MainActivity", "handleUiEvent: $event")
		when (event) {
			is UiEvent.ShowToast -> {
				val uiText = event.uiText.getString(this)
				if (uiText.isNotEmpty()) Toast.makeText(this, uiText, Toast.LENGTH_SHORT).show()
			}

			is UiEvent.Navigate -> navController.navigate(event.route.route)
			is UiEvent.PopBackStack -> navController.popBackStack()
			is UiEvent.SearchButtonClick -> uiStateDispatcher.toggleSearchBarActive()
			is UiEvent.Error -> handleErrorEvent(event)
			is UiEvent.UpdateUserInstance -> lifecycleScope.launch { authViewModel.initializeUser() }
		}
	}

	private fun handleErrorEvent(event: UiEvent.Error) {
		val message = event.response.detail
			?: event.throwable?.localizedMessage
			?: event.customMessageStringRes?.let { getString(it) }
			?: getString(R.string.toast_common_error)

		if (event.throwable !is CancellationException && message.isNotEmpty()) {
			Toast.makeText(this, message, Toast.LENGTH_LONG).show()
		}
	}
}