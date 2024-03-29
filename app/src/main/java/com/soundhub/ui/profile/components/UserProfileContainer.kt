package com.soundhub.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.data.model.Genre
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.profile.components.sections.favorite_genres.FavoriteGenresSection
import com.soundhub.ui.profile.components.sections.friend_list.FriendMiniatureList
import com.soundhub.ui.profile.components.sections.photos.UserPhotoCarousel
import com.soundhub.ui.profile.components.sections.user_actions.ProfileButtonsRow
import com.soundhub.ui.profile.components.sections.user_actions.UserNameWithDescription


@Composable
fun UserProfileContainer(
    user: User?,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val authorizedUser by authViewModel.userInstance.collectAsState()
    val isOriginProfile: Boolean = authorizedUser?.id?.toString() == user?.id?.toString()
    val userLocation: String = getUserLocation(user?.city, user?.country)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 30.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Column {
                UserNameWithDescription(user)
                // user location
                if (userLocation.isNotEmpty())
                    Text(
                        text = userLocation,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
            }

            ProfileButtonsRow(
                isOriginProfile = isOriginProfile,
                navController = navController,
                user = user
            )

            // friend list with fake data
            FriendMiniatureList(
                friendList = listOf(
                    FriendMiniatureItem(null),
                    FriendMiniatureItem(null),
                    FriendMiniatureItem(null),
                    FriendMiniatureItem(null),
                    FriendMiniatureItem(null),
                    FriendMiniatureItem(null),
                    FriendMiniatureItem(null),
                ),
                navController = navController
            )

            // genres list with fake data
            FavoriteGenresSection(
                genreList = listOf(
                    Genre(name = "pop rock"),
                    Genre(name = "alternative rock"),
                    Genre(name = "jazz"),
                    Genre(name = "pop"),
                    Genre(name = "hip-hop"),
                    Genre(name = "punk-rock"),
                    Genre(name = "post-hardcore")
                ),
                isOriginProfile = isOriginProfile,
                navController = navController
            )

            HorizontalDivider(thickness = 1.dp)

            // temporary user photos
            UserPhotoCarousel(
                images = listOf(
                    "https://play-lh.googleusercontent.com/y_-anVKl3ID25Je02J1dseqlAm41N8pwI-Gad7aDxPIPss3d7hUYF8c08SNCtwSPW5g",
                    "https://play-lh.googleusercontent.com/y_-anVKl3ID25Je02J1dseqlAm41N8pwI-Gad7aDxPIPss3d7hUYF8c08SNCtwSPW5g",
                    "https://play-lh.googleusercontent.com/y_-anVKl3ID25Je02J1dseqlAm41N8pwI-Gad7aDxPIPss3d7hUYF8c08SNCtwSPW5g"
                ),
                navController = navController
            )
        }
    }
}

fun getUserLocation(city: String?, country: String?): String {
    return if ((city == null && country == null) || (city!!.isEmpty() && country!!.isEmpty())) ""
    else if (country!!.isNotEmpty() && city.isEmpty()) country
    else "$country, $city"
}


data class FriendMiniatureItem(
    val avatarUrl: String?
)
