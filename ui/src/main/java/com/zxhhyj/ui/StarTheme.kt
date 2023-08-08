package com.zxhhyj.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf


val LocalColorScheme =
    compositionLocalOf<ColorScheme> { TODO() }

val LocalTextStyles = compositionLocalOf<StarTextStyles> { TODO() }

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