package com.soundhub.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color


val md_theme_light_primary = Color(0xFF6750A4)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFf3edf7)
val md_theme_light_onPrimaryContainer = Color(0xFF22005D)
val md_theme_light_secondary = Color(0xFF6750A4)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFE8DEF8)
val md_theme_light_onSecondaryContainer = Color(0xFF23005C)
val md_theme_light_tertiary = Color(0xFF7E5260)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFFD9E3)
val md_theme_light_onTertiaryContainer = Color(0xFF31101D)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFf7f2fa)
val md_theme_light_onBackground = Color(0xFF1C1B1E)
val md_theme_light_surface = Color(0xFFFFFBFF)
val md_theme_light_onSurface = Color(0xFF1C1B1E)
val md_theme_light_surfaceVariant = Color(0xFFE7E0EB)
val md_theme_light_onSurfaceVariant = Color(0xFF49454E)
val md_theme_light_outline = Color(0xFF7A757F)
val md_theme_light_inverseOnSurface = Color(0xFFF4EFF4)
val md_theme_light_inverseSurface = Color(0xFF313033)
val md_theme_light_inversePrimary = Color(0xFFCFBCFF)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF6750A4)
val md_theme_light_outlineVariant = Color(0xFFCAC4CF)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFCFBCFF)
val md_theme_dark_onPrimary = Color(0xFF381E72)
val md_theme_dark_primaryContainer = Color(0xFF211f26)
val md_theme_dark_onPrimaryContainer = Color(0xFFE9DDFF)
val md_theme_dark_secondary = Color(0xFFD0BCFF)
val md_theme_dark_onSecondary = Color(0xFF381E72)
val md_theme_dark_secondaryContainer = Color(0xFF4a4458)
val md_theme_dark_onSecondaryContainer = Color(0xFFE9DDFF)
val md_theme_dark_tertiary = Color(0xFFEFB8C8)
val md_theme_dark_onTertiary = Color(0xFF4A2532)
val md_theme_dark_tertiaryContainer = Color(0xFF633B48)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFD9E3)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF1C1B1E)
val md_theme_dark_onBackground = Color(0xFFE6E1E6)
val md_theme_dark_surface = Color(0xFF1C1B1E)
val md_theme_dark_onSurface = Color(0xFFE6E1E6)
val md_theme_dark_surfaceVariant = Color(0xFF49454E)
val md_theme_dark_onSurfaceVariant = Color(0xFFCAC4CF)
val md_theme_dark_outline = Color(0xFF948F99)
val md_theme_dark_inverseOnSurface = Color(0xFF1C1B1E)
val md_theme_dark_inverseSurface = Color(0xFFE6E1E6)
val md_theme_dark_inversePrimary = Color(0xFF6750A4)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFCFBCFF)
val md_theme_dark_outlineVariant = Color(0xFF49454E)
val md_theme_dark_scrim = Color(0xFF000000)

var borderColor: Color = Color(0xFFCAC4D0)

val seed = Color(0xFF6750A4)
val dark_acceptColor = Color(0xFF729C62)
val onDark_acceptColor = Color(0xFFFFFFFF)

val onLight_acceptColor = Color(0xFF000000)
val light_acceptColor = Color(0xFF577F49)

private var ColorSchemeAcceptColorMap = mutableMapOf<ColorScheme, Color>()
private var ColorSchemeOnAcceptColorMap = mutableMapOf<ColorScheme, Color>()

var ColorScheme.acceptColor: Color
	get() = ColorSchemeAcceptColorMap[this] ?: Color.Unspecified
	set(value) {
		ColorSchemeAcceptColorMap[this] = value
	}

var ColorScheme.onAcceptColor: Color
	get() = ColorSchemeOnAcceptColorMap[this] ?: Color.Unspecified
	set(value) {
		ColorSchemeOnAcceptColorMap[this] = value
	}

