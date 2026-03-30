package com.saas.fastbite.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val FastBiteColorScheme = lightColorScheme(
    primary = WarmAmber,
    onPrimary = Cream,
    secondary = DeepBrown,
    onSecondary = Cream,
    background = Cream,
    onBackground = DeepBrown,
    surface = Cream,
    onSurface = DeepBrown,
)

@Composable
fun FastBiteTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FastBiteColorScheme,
        typography = Typography,
        content = content
    )
}