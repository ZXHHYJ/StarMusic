package studio.mandysa.music.ui.theme

import android.app.WallpaperManager
import android.app.WallpaperManager.FLAG_SYSTEM
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LocalContentColor
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
import com.kyant.monet.*

@Composable
fun MandySaMusicTheme(
    content: @Composable () -> Unit
) {
    val defaultColor = Color.White
    //获取壁纸管理器
    val wallpaperManager = WallpaperManager.getInstance(LocalContext.current)
    //壁纸颜色
    var wallpaperColor: Color? by remember {
        mutableStateOf(null)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        wallpaperColor =
            try {
                Color(wallpaperManager.getWallpaperColors(FLAG_SYSTEM)!!.primaryColor.toArgb())
            } catch (e: Exception) {
                //windows sub system就会报错
                MaterialTheme.colorScheme.primary
            }
        DisposableEffect(key1 = null) {
            //判断是否支持获取壁纸颜色
            val listener = WallpaperManager.OnColorsChangedListener { colors, which ->
                if (FLAG_SYSTEM == which && colors != null) {
                    wallpaperColor = Color(colors.primaryColor.toArgb())
                }
            }
            wallpaperManager.addOnColorsChangedListener(
                listener,
                Handler.createAsync(Looper.getMainLooper())
            )
            onDispose {
                //避免内存泄露
                wallpaperManager.removeOnColorsChangedListener(listener)
            }
        }
    }
    val palettes = TonalPalettes(keyColor = wallpaperColor ?: defaultColor , style = PaletteStyle.TonalSpot)
    // In your Theme.kt
    CompositionLocalProvider(LocalTonalPalettes provides palettes) {
        CompositionLocalProvider(LocalContentColor provides 0.n1..100.n1) {
            MaterialTheme(colorScheme = dynamicColorScheme()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
            ) {
                content.invoke()
            }
          }
        }
    }
}