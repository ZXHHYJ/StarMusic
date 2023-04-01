package studio.mandysa.music.ui.theme

import android.app.WallpaperManager
import android.app.WallpaperManager.FLAG_SYSTEM
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.kyant.monet.*

/**
 * @author 黄浩
 */

@Composable
fun MandySaMusicTheme(
    content: @Composable () -> Unit
) {
    //获取壁纸管理器
    val wallpaperManager = WallpaperManager.getInstance(LocalContext.current)
    //壁纸颜色
    var wallpaperColor: Color by remember {
        mutableStateOf(Color.Red)
    }
    //判断是否支持获取壁纸颜色
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        try {
            wallpaperColor =
                Color(wallpaperManager.getWallpaperColors(FLAG_SYSTEM)!!.primaryColor.toArgb())
        } catch (e: Exception) {
            //windows sub system就会报错
        }
        DisposableEffect(Unit) {
            val colorsChangedListener = WallpaperManager.OnColorsChangedListener { colors, which ->
                if (FLAG_SYSTEM == which && colors != null) {
                    wallpaperColor = Color(colors.primaryColor.toArgb())
                }
            }
            wallpaperManager.addOnColorsChangedListener(
                colorsChangedListener,
                Handler.createAsync(Looper.getMainLooper())
            )
            onDispose {
                //避免内存泄露
                wallpaperManager.removeOnColorsChangedListener(colorsChangedListener)
            }
        }
    }
    val palettes =
        TonalPalettes(keyColor = wallpaperColor, style = PaletteStyle.Vibrant)
    // In your Theme.kt
    CompositionLocalProvider(LocalTonalPalettes provides palettes) {
        CompositionLocalProvider(LocalContentColor provides 0.n1..100.n1) {
            MaterialTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(appBackgroundColor)
                ) {
                    content.invoke()
                }
            }
        }
    }
}