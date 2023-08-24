package com.zxhhyj.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf


val LocalColorScheme =
    compositionLocalOf<ColorScheme> { throw RuntimeException() }

val LocalTextStyles = compositionLocalOf<StarTextStyles> { throw RuntimeException() }

@Composable
fun StarTheme(
    colorScheme: ColorScheme,
    textStyles: StarTextStyles,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
        LocalTextStyles provides textStyles
    ) {
        content()
    }
}