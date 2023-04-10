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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import androidx.lifecycle.map
import coil.ImageLoader
import coil.request.ImageRequest
import com.kyant.monet.*
import studio.mandysa.music.logic.repository.SettingRepository
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.ktx.coverUrl

@Composable
fun MandySaMusicTheme(
    content: @Composable () -> Unit
) {
    //获取壁纸管理器
    val wallpaperManager = WallpaperManager.getInstance(LocalContext.current)
    //monet颜色
    var monetColor: Color by remember {
        mutableStateOf(Color.Red)
    }
    when (SettingRepository.enableAlbumGetColor) {
        true -> {
            val context = LocalContext.current
            val coverUrl by PlayManager.changeMusicLiveData().map {
                it.coverUrl
            }.observeAsState("")
            LaunchedEffect(coverUrl) {
                val imageLoader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(coverUrl)
                    .build()
                monetColor =
                    imageLoader.execute(request).drawable?.toBitmap()?.scale(3, 3)?.asImageBitmap()
                        ?.toPixelMap()?.get(1, 1) ?: Color.Red
            }
        }

        false -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                //判断是否支持获取壁纸颜色
                try {
                    monetColor =
                        Color(wallpaperManager.getWallpaperColors(FLAG_SYSTEM)!!.primaryColor.toArgb())
                } catch (e: Exception) {
                    //windows sub system就会报错
                }
                DisposableEffect(Unit) {
                    val colorsChangedListener =
                        WallpaperManager.OnColorsChangedListener { colors, which ->
                            if (FLAG_SYSTEM == which && colors != null) {
                                monetColor = Color(colors.primaryColor.toArgb())
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
        }
    }
    val palettes =
        TonalPalettes(keyColor = monetColor, style = PaletteStyle.Vibrant)
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