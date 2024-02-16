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
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.state.AuthFormState
import com.soundhub.utils.Validator

@Composable
fun AuthForm(
    isBottomSheetHidden: Boolean,
    authViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val density: Density = LocalDensity.current
    val context: Context = LocalContext.current

    val authFormState = authViewModel.authFormState.collectAsState().value
    var buttonFormText: String = if (authFormState.isRegisterForm)
        stringResource(id = R.string.auth_button_register_name)
    else stringResource(id = R.string.auth_button_login_name)

    // if bottom sheet is hidden typed data are deleted
    if (isBottomSheetHidden) authViewModel.resetAuthFormState()
    if (!authFormState.isRegisterForm) authViewModel.resetRepeatedPassword()

    // it works every time when isRegisterForm variable is changed
    LaunchedEffect(key1 = authFormState.isRegisterForm) {
        buttonFormText = if (authFormState.isRegisterForm)
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
            supportingText = { ErrorPasswordFieldColumn(authFormState) }
        )

        // repeat password field
        AnimatedVisibility(
            visible = authFormState.isRegisterForm,
            enter = slideInVertically(initialOffsetY = { with(density) { -40.dp.roundToPx() } }) +
                    expandVertically(expandFrom = Alignment.Top) +
                    fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically(targetOffsetY = { with(density) { -40.dp.roundToPx() } }) +
                    shrinkVertically(shrinkTowards = Alignment.Top) +
                    fadeOut()
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = authFormState.repeatedPassword ?: "",
                singleLine = true,
                onValueChange = authViewModel::setRepeatedPassword,
                label = { Text(stringResource(id = R.string.repeat_password_label)) },
                isError = !authFormState.isPasswordValid || !authFormState.arePasswordsEqual,
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text(stringResource(R.string.password_placeholder)) },
                supportingText = { ErrorPasswordFieldColumn(authFormState) }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Switch(
                checked = authFormState.isRegisterForm,
                onCheckedChange = authViewModel::setAuthFormType
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
            onClick = authViewModel::authAction,
            enabled = Validator.validateAuthForm(authFormState)
        ) {
            Text(
                text = buttonFormText,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ErrorPasswordFieldColumn(authFormState: AuthFormState) {
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