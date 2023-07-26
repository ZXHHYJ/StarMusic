package com.zxhhyj.music.ui.theme

import android.app.WallpaperManager
import android.app.WallpaperManager.FLAG_SYSTEM
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.kyant.monet.LocalTonalPalettes
import com.kyant.monet.PaletteStyle
import com.kyant.monet.TonalPalettes
import com.kyant.monet.n1
import com.kyant.monet.rangeTo
import com.zxhhyj.music.logic.repository.SettingRepository

@Composable
fun MandySaMusicTheme(
    content: @Composable () -> Unit
) {
    //monet颜色
    var monetColor: Color by remember {
        mutableStateOf(Color.Red)
    }
    //莫奈取色
    if (SettingRepository.EnableMonet) {
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
            val handler = Handler(Looper.getMainLooper())
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
    } else {
        monetColor = Color.Red
    }
    CompositionLocalProvider(
        LocalTonalPalettes provides TonalPalettes(
            keyColor = monetColor,
            style = PaletteStyle.Vibrant
        ),
        LocalContentColor provides 0.n1..100.n1
    ) {
        MaterialTheme {
            Box(modifier = Modifier.background(appBackgroundColor)) {
                content.invoke()
            }
        }
    }
}