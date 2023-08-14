package com.zxhhyj.music.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.zxhhyj.ui.theme.StarTheme
import com.zxhhyj.ui.theme.starTextStyles

@Composable
fun MandySaMusicTheme(
    content: @Composable () -> Unit
) {
    StarTheme(
        colorScheme = if (isSystemInDarkTheme()) nightColorScheme else lightColorScheme,
        textStyles = starTextStyles()
    ) {
        MaterialTheme {
            content()
        }
    }
}