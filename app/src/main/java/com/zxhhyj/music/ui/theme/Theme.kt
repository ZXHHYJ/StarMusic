package com.zxhhyj.music.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.StarTheme
import com.zxhhyj.ui.theme.starTextStyles

@Composable
fun StarMusicTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = when (SettingRepository.ThemeMode) {
        SettingRepository.ThemeModeEnum.AUTO.value -> if (isSystemInDarkTheme()) nightColorScheme else lightColorScheme
        SettingRepository.ThemeModeEnum.LIGHT.value -> lightColorScheme
        SettingRepository.ThemeModeEnum.DARK.value -> nightColorScheme
        else -> if (isSystemInDarkTheme()) nightColorScheme else lightColorScheme
    }
    StarTheme(
        colorScheme = colorScheme,
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