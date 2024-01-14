package com.soundhub.ui.authentication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.UserStore
import com.soundhub.data.model.User
import com.soundhub.data.repository.AuthRepository
import com.soundhub.ui.authentication.state.AuthValidationState
import com.soundhub.ui.mainActivity.MainViewModel
import com.soundhub.utils.Routes
import com.soundhub.utils.UiEvent
import com.soundhub.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val mainViewModel: MainViewModel,
    private val userStore: UserStore
): ViewModel() {
    val userCreds: Flow<Preferences> = userStore.getCreds()

    var authValidationState by mutableStateOf(AuthValidationState())
        private set

    private var _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: MutableState<Boolean> get() = _isLoggedIn

    init {
        viewModelScope.launch {
            _isLoggedIn.value = userStore.ifUserStored()

            if (authValidationState.email.isEmpty() && authValidationState.password.isEmpty())
                authValidationState = authValidationState.copy(isEmailValid = true, isPasswordValid = true)
        }
    }

    fun resetState() {
        viewModelScope.launch {
            authValidationState = AuthValidationState()
        }
    }

    fun validateForm(): Boolean {
        val isLoginFormValid =  authValidationState.isEmailValid
                && authValidationState.isPasswordValid
                && authValidationState.email.isNotEmpty()
                && authValidationState.password.isNotEmpty()

        if (authValidationState.isRegisterForm)
            return isLoginFormValid
                    && authValidationState.arePasswordsEqual
                    && authValidationState.repeatedPassword?.isNotEmpty() ?: false
        return isLoginFormValid

    }

    fun resetRepeatedPassword() {
        viewModelScope.launch {
            authValidationState = authValidationState.copy(repeatedPassword = null)
        }
    }

    fun onEmailTextFieldChange(value: String) {
        viewModelScope.launch {
            val isEmailValid = Validator.validateEmail(value)
            authValidationState = authValidationState.copy(
                email = value,
                isEmailValid = isEmailValid,
            )
        }
    }

    fun onPasswordTextFieldChange(value: String) {
        viewModelScope.launch {
            val isPasswordValid: Boolean = Validator.validatePassword(value)
            authValidationState = authValidationState.copy(
                password = value,
                isPasswordValid = isPasswordValid,
            )
        }
    }

    fun onRepeatedPasswordTextFieldChange(value: String) {
        viewModelScope.launch {
            val arePasswordsEqual: Boolean = Validator
                .arePasswordsEqual(authValidationState.password, value)

            authValidationState = authValidationState.copy(
                repeatedPassword = value,
                arePasswordsEqual = arePasswordsEqual
            )
        }
    }

    fun onAuthTypeSwitchChange(value: Boolean) {
        viewModelScope.launch {
            authValidationState = authValidationState.copy(
                isRegisterForm = value
            )
        }
    }

    fun onFormButtonClick() {
        if (authValidationState.isRegisterForm)
            onEvent(AuthEvent.OnRegister(
                User(email = authValidationState.email, password = authValidationState.password)
            ))
        else
            onEvent(AuthEvent.OnLogin(authValidationState.email, authValidationState.password))
    }

    fun onLogoutButtonClick() {
        onEvent(AuthEvent.OnLogout)
    }

    private fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnLogin -> {
                viewModelScope.launch {
                    mainViewModel.sendUiEvent(UiEvent.ShowToast(
                        message = "You successfully logged in!\n" +
                                "Your data: {email: ${event.email}}"
                    ))

                    val user: User? = authRepository.login(event.email, event.password)
                    if (user != null) {
                        userStore.saveUser(event.email, event.password)

                        mainViewModel.sendUiEvent(UiEvent.Navigate(Routes.Authenticated))
                        _isLoggedIn.value = true
                    }
                    mainViewModel.sendUiEvent(UiEvent.ShowToast("Неправильный логин или пароль"))
                }
            }

            is AuthEvent.OnRegister -> {
                viewModelScope.launch {
                    mainViewModel.onEvent(UiEvent.ShowToast(
                        message = "You successfully signed up!\nYour data ${event.user.email}",
                    ))
                    authRepository.registerUser(event.user)
                    userStore.saveUser(event.user.email, event.user.name)
                    _isLoggedIn.value = true

                    mainViewModel.onEvent(UiEvent.Navigate(Routes.Authenticated))
                }
            }

            is AuthEvent.OnLogout -> {
                viewModelScope.launch {
                    userStore.clear()
                    mainViewModel.onEvent(UiEvent.Navigate(Routes.AppStart))
                }
            }
        }
    }
}