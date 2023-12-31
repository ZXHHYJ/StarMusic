package com.zxhhyj.music.ui.theme

import android.app.WallpaperManager
import android.app.WallpaperManager.FLAG_SYSTEM
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
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
    val isDark = when (SettingRepository.DarkMode) {
        SettingRepository.DarkModeEnum.AUTO -> isSystemInDarkTheme()
        SettingRepository.DarkModeEnum.LIGHT -> false
        SettingRepository.DarkModeEnum.DARK -> true
    }

    val colorScheme = when (SettingRepository.ThemeMode) {
        SettingRepository.ThemeModeEnum.DEFAULT -> {
            if (isDark) nightColorScheme else lightColorScheme
        }

        SettingRepository.ThemeModeEnum.MONET -> {
            val mainColor = mainColor
            var monetColor: Color by remember {
                mutableStateOf(mainColor)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                //获取壁纸管理器
                val wallpaperManager = WallpaperManager.getInstance(LocalContext.current)
                //判断是否支持获取壁纸颜色
                try {
                    monetColor =
                        Color(wallpaperManager.getWallpaperColors(FLAG_SYSTEM)!!.primaryColor.toArgb())
                } catch (e: Exception) {
                    //windows sub system就会报错
                }
                val handler = Handler.createAsync(Looper.getMainLooper())
                DisposableEffect(Unit) {
                    val colorsChangedListener =
                        WallpaperManager.OnColorsChangedListener { colors, which ->
                            if (FLAG_SYSTEM == which && colors != null) {
                                monetColor = Color(colors.primaryColor.toArgb())
                            }
                        }

                    wallpaperManager.addOnColorsChangedListener(
                        colorsChangedListener,
                        handler
                    )
                    onDispose {
                        //避免内存泄露
                        wallpaperManager.removeOnColorsChangedListener(colorsChangedListener)
                    }
                }
            }

            val hct = Hct.fromInt(monetColor.toArgb())
            val colors = MaterialDynamicColors()
            val scheme = when (SettingRepository.MonetPaletteStyle) {
                PaletteStyle.TonalSpot -> SchemeTonalSpot(
                    hct,
                    isDark,
                    0.0
                )

                PaletteStyle.Neutral -> SchemeNeutral(
                    hct,
                    isDark,
                    0.0
                )

                PaletteStyle.Vibrant -> SchemeVibrant(
                    hct,
                    isDark,
                    0.0
                )

                PaletteStyle.Expressive -> SchemeExpressive(
                    hct,
                    isDark,
                    0.0
                )

                PaletteStyle.Rainbow -> SchemeRainbow(
                    hct,
                    isDark,
                    0.0
                )

                PaletteStyle.FruitSalad -> SchemeFruitSalad(
                    hct,
                    isDark,
                    0.0
                )

                PaletteStyle.Monochrome -> SchemeMonochrome(
                    hct,
                    isDark,
                    0.0
                )

                PaletteStyle.Fidelity -> SchemeFidelity(
                    hct,
                    isDark,
                    0.0
                )

                PaletteStyle.Content -> SchemeContent(
                    hct,
                    isDark,
                    0.0
                )
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