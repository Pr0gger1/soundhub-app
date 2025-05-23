package com.soundhub.presentation.pages.authentication.ui.forms

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.presentation.pages.authentication.AuthFormState
import com.soundhub.presentation.pages.authentication.AuthenticationViewModel
import com.soundhub.presentation.pages.registration.RegistrationViewModel
import com.soundhub.presentation.shared.loaders.CircleLoader
import com.soundhub.utils.lib.AuthValidator

@Composable
fun AuthForm(
	isBottomSheetHidden: Boolean,
	authViewModel: AuthenticationViewModel,
	registrationViewModel: RegistrationViewModel
) {
	val density: Density = LocalDensity.current
	val context: Context = LocalContext.current
	val passwordFieldOffsetY = with(density) { -40.dp.roundToPx() }

	val authFormState by authViewModel.authFormState.collectAsState()
	var buttonFormText: String = getButtonFormText(authFormState.isRegisterForm, context)

	if (isBottomSheetHidden) authViewModel.resetAuthFormState()
	if (!authFormState.isRegisterForm) authViewModel.resetRepeatedPassword()

	LaunchedEffect(key1 = authFormState.isRegisterForm) {
		buttonFormText = getButtonFormText(authFormState.isRegisterForm, context)
	}

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.animateContentSize()
			.padding(start = 30.dp, end = 30.dp, bottom = 20.dp),
		verticalArrangement = Arrangement.spacedBy(5.dp)
	) {

		// email field
		OutlinedTextField(
			modifier = Modifier.fillMaxWidth(),
			value = authFormState.email,
			singleLine = true,
			onValueChange = authViewModel::setEmail,
			label = { Text(stringResource(id = R.string.email_label)) },
			isError = !authFormState.isEmailValid,
			supportingText = {
				if (!authFormState.isEmailValid)
					Text(
						text = stringResource(id = R.string.invalid_email),
						color = MaterialTheme.colorScheme.error
					)
			},
			placeholder = { Text(stringResource(R.string.email_placeholder)) },
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
		)

		// password field
		OutlinedTextField(
			modifier = Modifier.fillMaxWidth(),
			value = authFormState.password,
			singleLine = true,
			onValueChange = authViewModel::setPassword,
			label = { Text(stringResource(id = R.string.password_label)) },
			isError = !authFormState.isPasswordValid || !authFormState.arePasswordsEqual,
			visualTransformation = PasswordVisualTransformation(),
			placeholder = { Text(stringResource(R.string.password_placeholder)) },
			supportingText = { ErrorPasswordFieldMessage(authFormState) }
		)

		// repeated password field
		AnimatedVisibility(
			visible = authFormState.isRegisterForm,
			enter = slideInVertically(initialOffsetY = { passwordFieldOffsetY }) +
					expandVertically(expandFrom = Alignment.Top) +
					fadeIn(initialAlpha = 0.3f),
			exit = slideOutVertically(targetOffsetY = { passwordFieldOffsetY }) +
					shrinkVertically(shrinkTowards = Alignment.Top) +
					fadeOut()
		) {
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = authFormState.repeatedPassword,
				singleLine = true,
				onValueChange = authViewModel::setRepeatedPassword,
				label = { Text(stringResource(id = R.string.repeat_password_label)) },
				isError = !authFormState.isPasswordValid || !authFormState.arePasswordsEqual,
				visualTransformation = PasswordVisualTransformation(),
				placeholder = { Text(stringResource(R.string.password_placeholder)) },
				supportingText = { ErrorPasswordFieldMessage(authFormState) }
			)
		}

		AuthFormSwitch(
			isRegisterForm = authFormState.isRegisterForm,
			onCheckedChange = authViewModel::setAuthFormType
		)

		FilledTonalButton(
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp),
			shape = RoundedCornerShape(5.dp),
			colors = ButtonDefaults.buttonColors(),
			onClick = {
				onSubmitButtonClick(
					authFormState = authFormState,
					registrationViewModel = registrationViewModel,
					authViewModel = authViewModel
				)
			},
			enabled = AuthValidator.validateAuthForm(authFormState)
		) {
			if (authFormState.isLoading)
				CircleLoader(modifier = Modifier.size(24.dp))
			else Text(
				text = buttonFormText,
				fontFamily = FontFamily(Font(R.font.nunito_bold)),
				fontSize = 14.sp,
				fontWeight = FontWeight.Bold
			)
		}
	}
}


@Composable
private fun ErrorPasswordFieldMessage(authFormState: AuthFormState) {
	Column {
		if (!authFormState.isPasswordValid)
			Text(
				text = stringResource(id = R.string.invalid_password),
				color = MaterialTheme.colorScheme.error
			)
		if (!authFormState.arePasswordsEqual && authFormState.isRegisterForm)
			Text(
				text = stringResource(id = R.string.passwords_mismatch),
				color = MaterialTheme.colorScheme.error
			)
	}
}

private fun onSubmitButtonClick(
	authFormState: AuthFormState,
	registrationViewModel: RegistrationViewModel,
	authViewModel: AuthenticationViewModel
) {
	if (authFormState.isRegisterForm)
		registrationViewModel.onSignUpButtonClick(authFormState)
	else authViewModel.signIn()
}

private fun getButtonFormText(isRegisterForm: Boolean, context: Context): String {
	return if (isRegisterForm)
		context.getString(R.string.auth_button_register_name)
	else context.getString(R.string.auth_button_login_name)
}