package com.soundhub.ui.edit_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.forms.UserDataForm

@Composable
fun EditUserProfileScreen(
    editUserProfileViewModel: EditUserProfileViewModel = hiltViewModel(),
    authViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val formState = editUserProfileViewModel.formState.collectAsState()

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        UserDataForm(
            formState = formState,
            onFirstNameChange = editUserProfileViewModel::onFirstNameChange,
            onLastNameChange = editUserProfileViewModel::onLastNameChange,
            onBirthdayChange = editUserProfileViewModel::onBirthdateChange,
            onDescriptionChange = editUserProfileViewModel::onDescriptionChange,
            onGenderChange = editUserProfileViewModel::onGenderChange,
            onCountryChange = editUserProfileViewModel::onCountryChange
        )
    }
}

@Composable
@Preview
fun EditUserProfileScreenPreview() {
    EditUserProfileScreen()
}