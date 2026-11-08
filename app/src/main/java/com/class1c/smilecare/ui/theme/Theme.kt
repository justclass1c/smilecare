package com.class1c.smilecare.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SmileDarkPrimary,
    onPrimary = SmileDarkOnPrimary,
    primaryContainer = SmileDarkPrimaryContainer,
    onPrimaryContainer = SmileDarkOnPrimaryContainer,
    secondary = SmileTealContainer,
    onSecondary = SmileTealDark,
    secondaryContainer = SmileDarkSurfaceVariant,
    onSecondaryContainer = SmileDarkOnSurface,
    tertiary = SmileTealContainer,
    onTertiary = SmileTealDark,
    tertiaryContainer = SmileDarkSurfaceVariant,
    onTertiaryContainer = SmileDarkOnSurface,
    background = SmileDarkBackground,
    onBackground = SmileDarkOnSurface,
    surface = SmileDarkSurface,
    onSurface = SmileDarkOnSurface,
    surfaceVariant = SmileDarkSurfaceVariant,
    onSurfaceVariant = SmileDarkOnSurfaceVariant,
    outline = SmileDarkOnSurfaceVariant,
    outlineVariant = SmileDarkSurfaceVariant,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

private val LightColorScheme = lightColorScheme(
    primary = SmileTeal,
    onPrimary = Color.White,
    primaryContainer = SmileTealContainer,
    onPrimaryContainer = SmileOnTealContainer,
    secondary = SmileTealDark,
    onSecondary = Color.White,
    secondaryContainer = SmileInfo,
    onSecondaryContainer = SmileText,
    tertiary = SmileTeal,
    onTertiary = Color.White,
    tertiaryContainer = SmileWarningContainer,
    onTertiaryContainer = SmileWarning,
    background = SmileBackground,
    onBackground = SmileText,
    surface = SmileSurface,
    onSurface = SmileText,
    surfaceVariant = SmileInfo,
    onSurfaceVariant = SmileMuted,
    outline = SmileOutline,
    outlineVariant = SmileBorder,
    error = SmileError,
    onError = Color.White,
    errorContainer = SmileErrorContainer,
    onErrorContainer = SmileError,
    inverseSurface = SmileText,
    inverseOnSurface = SmileBackground,
    inversePrimary = SmileTealContainer,
    scrim = Color.Black,
)

@Composable
fun SmileCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // The SmileCare palette is part of the product identity, so dynamic colour
    // is opt-in rather than the default.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
