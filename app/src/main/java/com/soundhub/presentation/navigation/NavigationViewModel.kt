package com.soundhub.presentation.navigation

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.soundhub.Route
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.domain.model.User
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
	private val uiStateDispatcher: UiStateDispatcher,
	private val userDao: UserDao,
	userCredsStore: UserCredsStore
) : ViewModel() {
	private val userCreds = userCredsStore.getCreds()

	suspend fun onNavDestinationChangedListener(
		controller: NavController,
		destination: NavDestination,
	) {
		val userCreds = userCreds.firstOrNull()
		val backStackEntry: NavBackStackEntry? = controller.currentBackStackEntry
		val navArguments: Bundle? = backStackEntry?.arguments
		val route: String? = destination.route

		Log.d("NavigationUtils", "onNavDestinationChangedListener[current_route]: $route")

		checkAuthAndNavigate(
			route = route,
			userCreds = userCreds,
			controller = controller
		)

		updateCurrentRouteState(
			appBundleState = navArguments,
			uiStateDispatcher = uiStateDispatcher,
			route = route
		)
		uiStateDispatcher.setSearchBarActive(false)
	}

	private suspend fun checkAuthAndNavigate(
		route: String?,
		userCreds: UserPreferences?,
		controller: NavController
	) {
		val userInstance: User? = userDao.getCurrentUser()
		val isUserAuthorized: Boolean = userCreds?.isValid() == true
				&& userInstance != null

		val isAuthRoute: Boolean = route?.contains(Route.Authentication.route) == true

		val targetRoute: String? = when {
			!(isUserAuthorized || isAuthRoute) -> Route.Authentication.route
			isUserAuthorized && isAuthRoute -> Route.PostLine.route
			else -> null
		}

		if (targetRoute != null && targetRoute != route) {
			controller.navigate(targetRoute)
		}
	}

	private fun updateCurrentRouteState(
		appBundleState: Bundle?,
		route: String?,
		uiStateDispatcher: UiStateDispatcher
	) {
		var currentRoute = route
		val argument: String? = appBundleState
			?.keySet()
			?.firstOrNull { it in Constants.ALL_NAV_ARGS }

		// here we replace raw {NAV_ARG} to real nav argument
		// for example user/1234 instead of user/{userId}
		appBundleState?.let { bundle ->
			currentRoute = Route.replaceNavArgTemplate(
				navArg = bundle.getString(argument) ?: "",
				route = route ?: ""
			)
		}

		uiStateDispatcher.setCurrentRoute(currentRoute)
	}
}