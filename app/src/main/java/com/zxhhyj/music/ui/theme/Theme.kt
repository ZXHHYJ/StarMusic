package com.zxhhyj.music.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.zxhhyj.ui.StarTheme
import com.zxhhyj.ui.starTextStyles

@Composable
fun MandySaMusicTheme(
    content: @Composable () -> Unit
) {
    StarTheme(colorScheme = lightColorScheme, textStyles = starTextStyles()) {
        MaterialTheme {
            content.invoke()
        }
    }
}