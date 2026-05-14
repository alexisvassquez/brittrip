package com.alexisvassquez.brittrip.ui.theme

import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BritTripColorScheme = lightColorScheme(
    primary = NavyBlue,
    onPrimary = CardWhite,
    secondary = TubeRed,
    onSecondary = CardWhite,
    background = OffWhite,
    onBackground = NavyBlue,
    surface = CardWhite,
    onSurface = NavyBlue,
    surfaceVariant = LightSteel,
    onSurfaceVariant = NavyBlue,
    error = ErrorRed,
    onError = CardWhite
)

@Composable
fun BritTripTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BritTripColorScheme,
        typography = Typography,
        content = content
    )
}