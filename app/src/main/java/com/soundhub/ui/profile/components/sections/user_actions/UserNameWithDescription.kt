package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.utils.DateFormatter
import java.time.LocalDate

@Composable
internal fun UserNameWithDescription(profileOwner: User?) {
    var userFullName by rememberSaveable { mutableStateOf("") }
    var isDescriptionButtonChecked: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = profileOwner) {
        userFullName = "${profileOwner?.firstName} ${profileOwner?.lastName}".trim()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = userFullName,
            fontWeight = FontWeight.Bold,
            lineHeight = 16.sp,
            fontSize = 28.sp,
            modifier = Modifier.clickable {
                isDescriptionButtonChecked = !isDescriptionButtonChecked
            }
        )

        IconToggleButton(
            checked = isDescriptionButtonChecked,
            onCheckedChange = { isDescriptionButtonChecked = it },
        ) {
            Icon(
                imageVector = if (isDescriptionButtonChecked) Icons.Rounded.KeyboardArrowUp
                else Icons.Rounded.KeyboardArrowDown,
                contentDescription = "expand description",
                modifier = Modifier.size(35.dp)
            )
        }
    }
    if (isDescriptionButtonChecked)
        UserDetailsSection(user = profileOwner)
}


@Composable
private fun UserDetailsSection(
    modifier: Modifier = Modifier,
    user: User? = null
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        if (user?.description?.isNotEmpty() == true)
            UserDescriptionBlock(description = user.description)

        user?.birthday?.let { UserBirthdayBlock(birthday = it) }

        if (user?.languages?.isNotEmpty() == true)
            UserLanguagesBlock(languages = user.languages)
    }
}

@Composable
private fun UserDescriptionBlock(description: String?) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            ) {
                append(stringResource(id = R.string.profile_screen_user_description))
            }
            append(" $description")
        },
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun UserBirthdayBlock(birthday: LocalDate) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary
            )
            ) {
                append(stringResource(id = R.string.profile_screen_user_birthday))
            }
            append(" " + DateFormatter.getStringDate(date = birthday, includeYear = true))
        },
        fontWeight = FontWeight.Medium,
    )
}

@Composable
private fun UserLanguagesBlock(languages: List<String>) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary
            )
            ) {
                append(stringResource(id = R.string.profile_screen_user_languages))
            }
            append(" ${languages.joinToString(", ")}")
        }
    )
}