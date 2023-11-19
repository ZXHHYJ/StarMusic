package com.zxhhyj.music.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import com.kyant.m3color.dynamiccolor.MaterialDynamicColors
import com.kyant.m3color.hct.Hct
import com.kyant.m3color.scheme.SchemeContent
import com.kyant.m3color.scheme.SchemeExpressive
import com.kyant.m3color.scheme.SchemeFidelity
import com.kyant.m3color.scheme.SchemeFruitSalad
import com.kyant.m3color.scheme.SchemeMonochrome
import com.kyant.m3color.scheme.SchemeNeutral
import com.kyant.m3color.scheme.SchemeRainbow
import com.kyant.m3color.scheme.SchemeTonalSpot
import com.kyant.m3color.scheme.SchemeVibrant
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.theme.ColorScheme
import com.zxhhyj.ui.theme.StarTheme
import com.zxhhyj.ui.theme.starTextStyles

@Composable
fun StarMusicTheme(
    content: @Composable () -> Unit
) {
    val isDark = when (SettingRepository.ThemeModeEnum.entries[SettingRepository.ThemeMode]) {
        SettingRepository.ThemeModeEnum.AUTO -> isSystemInDarkTheme()
        SettingRepository.ThemeModeEnum.LIGHT -> false
        SettingRepository.ThemeModeEnum.DARK -> true
    }

    val colorScheme = when (SettingRepository.EnableDynamicColors) {
        true -> {
            val keyColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                colorResource(id = android.R.color.system_accent1_500)
            } else {
                mainColor
            }

            val contrastLevel = 0.0
            val hct = Hct.fromInt(keyColor.toArgb())
            val colors = MaterialDynamicColors()
            val scheme = when (PaletteStyle.entries[SettingRepository.M3PaletteStyle]) {
                PaletteStyle.TonalSpot -> SchemeTonalSpot(hct, isDark, contrastLevel)
                PaletteStyle.Neutral -> SchemeNeutral(hct, isDark, contrastLevel)
                PaletteStyle.Vibrant -> SchemeVibrant(hct, isDark, contrastLevel)
                PaletteStyle.Expressive -> SchemeExpressive(hct, isDark, contrastLevel)
                PaletteStyle.Rainbow -> SchemeRainbow(hct, isDark, contrastLevel)
                PaletteStyle.FruitSalad -> SchemeFruitSalad(hct, isDark, contrastLevel)
                PaletteStyle.Monochrome -> SchemeMonochrome(hct, isDark, contrastLevel)
                PaletteStyle.Fidelity -> SchemeFidelity(hct, isDark, contrastLevel)
                PaletteStyle.Content -> SchemeContent(hct, isDark, contrastLevel)
            }
            ColorScheme(
                highlight = Color(colors.primary().getArgb(scheme)),
                background = Color(colors.background().getArgb(scheme)),
                highBackground = Color(colors.secondaryContainer().getArgb(scheme)),
                outline = Color(colors.outline().getArgb(scheme)),
                text = Color(colors.onSecondaryContainer().getArgb(scheme)),
                onText = Color(colors.background().getArgb(scheme)),
                subText = Color(colors.onSecondaryContainer().getArgb(scheme)).copy(alpha = 0.6f),
                disabled = Color(colors.onSecondaryContainer().getArgb(scheme)).copy(alpha = 0.8f)
            )
        }

        false -> if (isDark) nightColorScheme else lightColorScheme
    }

    StarTheme(
        colorScheme = colorScheme,
        textStyles = starTextStyles()
    ) {
        MaterialTheme {
            content()
        }
    }
}