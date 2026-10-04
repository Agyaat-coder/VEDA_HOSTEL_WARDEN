package com.devesh.vedahostelwarden.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val VedaDarkColorScheme = darkColorScheme(
    primary = VedaBluePrimary,
    onPrimary = VedaTextPrimary,
    primaryContainer = VedaBlueDark,
    onPrimaryContainer = VedaTextPrimary,
    secondary = VedaBlueLight,
    onSecondary = VedaTextPrimary,
    background = VedaNavyBackground,
    onBackground = VedaTextPrimary,
    surface = VedaNavySurface,
    onSurface = VedaTextPrimary,
    surfaceVariant = VedaCardBackground,
    onSurfaceVariant = VedaTextSecondary,
    outline = VedaCardBorder,
    error = VedaDangerRed,
    onError = VedaTextPrimary
)

@Composable
fun VEDAHOSTELWARDENTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = VedaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
