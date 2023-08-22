package com.zxhhyj.music.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.ui.theme.LocalColorScheme
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalColorScheme.current.background)
        ) {
            MaterialTheme {
                content()
            }
        }
    }
}