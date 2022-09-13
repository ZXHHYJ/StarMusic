package sudio.mandysa.mytheme.theme

import android.app.WallpaperManager
import android.app.WallpaperManager.FLAG_SYSTEM
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.kyant.monet.LocalTonalPalettes
import com.kyant.monet.PaletteStyle
import com.kyant.monet.TonalPalettes.Companion.toTonalPalettes

private val M3TonalValues = doubleArrayOf(
    0.0, 10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 85.0, 90.0, 95.0, 99.0, 100.0
)

@Composable
fun MyTheme(
    keyColor: Color,
    style: PaletteStyle = PaletteStyle.TonalSpot,
    tonalValues: DoubleArray = M3TonalValues,
    colors: Colors = MaterialTheme.colors,
    typography: Typography = MaterialTheme.typography,
    shapes: Shapes = MaterialTheme.shapes,
    content: @Composable () -> Unit
) {
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
                keyColor
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
    //配置调色板
    val palettes = wallpaperColor?.toTonalPalettes(style = style, tonalValues = tonalValues)
    palettes?.let {
        CompositionLocalProvider(LocalTonalPalettes provides palettes) {
            MaterialTheme(
                colors = colors,
                typography = typography,
                shapes = shapes,
                content = content,
            )
        }
    }
}