package com.sangpal.quiz.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Orange500,
    primaryVariant = Orange400,
    secondary = Yellow200,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White
)

private val LightColorPalette = lightColors(
    primary = Orange400,
    primaryVariant = Orange500,
    secondary = Yellow200,
    onPrimary = Grey,
    onSecondary = Grey,
    onBackground = Grey,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun QuizTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}