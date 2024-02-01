package com.soundhub.ui.authentication.components

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.state.AuthFormState

@Composable
fun AuthForm(
    isBottomSheetHidden: Boolean,
    authViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val density: Density = LocalDensity.current
    val context: Context = LocalContext.current
    val authFormState: State<AuthFormState> = authViewModel.authFormState.collectAsState()
    var buttonFormText: String = if (authFormState.value.isRegisterForm) stringResource(id = R.string.auth_button_register_name)
    else stringResource(id = R.string.auth_button_login_name)

    // if bottom sheet is hidden typed data are deleted
    if (isBottomSheetHidden) authViewModel.resetAuthFormState()
    if (!authFormState.value.isRegisterForm) authViewModel.resetRepeatedPassword()

    // it works every time when isRegisterForm variable is changed
    LaunchedEffect(key1 = authFormState.value.isRegisterForm) {
        buttonFormText = if (authFormState.value.isRegisterForm)
            context.getString(R.string.auth_button_register_name)
        else context.getString(R.string.auth_button_login_name)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        // email field
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = authFormState.value.email,
            singleLine = true,
            onValueChange = { value -> authViewModel.setEmail(value) },
            label = { Text("Email") },
            isError = !authFormState.value.isEmailValid,
            supportingText = {
                if (!authFormState.value.isEmailValid)
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
            value = authFormState.value.password,
            singleLine = true,
            onValueChange = { value -> authViewModel.setPassword(value) },
            label = { Text(stringResource(id = R.string.password_label)) },
            isError = !authFormState.value.isPasswordValid || !authFormState.value.arePasswordsEqual,
            visualTransformation = PasswordVisualTransformation(),
            placeholder = { Text(stringResource(R.string.password_placeholder)) },
            supportingText = { ErrorPasswordFieldColumn(authFormState.value) }
        )

        // repeat password field
        AnimatedVisibility(
            visible = authFormState.value.isRegisterForm,
            enter = slideInVertically(initialOffsetY = { with(density) { -40.dp.roundToPx() } }) +
                    expandVertically(expandFrom = Alignment.Top) +
                    fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically(targetOffsetY = { with(density) { -40.dp.roundToPx() } }) +
                    shrinkVertically(shrinkTowards = Alignment.Top) +
                    fadeOut()
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = authFormState.value.repeatedPassword ?: "",
                singleLine = true,
                onValueChange = { value -> authViewModel.setRepeatedPassword(value) },
                label = { Text(stringResource(id = R.string.repeat_password_label)) },
                isError = !authFormState.value.isPasswordValid || !authFormState.value.arePasswordsEqual,
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text(stringResource(R.string.password_placeholder)) },
                supportingText = { ErrorPasswordFieldColumn(authFormState.value) }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Switch(
                checked = authFormState.value.isRegisterForm,
                onCheckedChange = { state -> authViewModel.onAuthTypeSwitchChange(state) }
            )
            Text(
                text = stringResource(R.string.get_account_switch_label),
                fontWeight = FontWeight.Bold
            )
        }

        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(),
            onClick = { authViewModel.authAction() },
            enabled = authViewModel.validateAuthForm()
        ) { Text(text = buttonFormText) }
    }
}

@Composable
fun ErrorPasswordFieldColumn(authFormState: AuthFormState) {
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