package com.zxhhyj.music.ui.theme

import android.app.WallpaperManager
import android.app.WallpaperManager.FLAG_SYSTEM
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.map
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import com.kyant.monet.LocalTonalPalettes
import com.kyant.monet.PaletteStyle
import com.kyant.monet.TonalPalettes
import com.kyant.monet.n1
import com.kyant.monet.rangeTo
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.service.playmanager.PlayManager

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
    when (SettingRepository.EnableAlbumGetColor) {
        true -> {
            val context = LocalContext.current
            val coverUrl by PlayManager.changeMusicLiveData().map {
                it?.album?.coverUrl
            }.observeAsState()
            LaunchedEffect(coverUrl) {
                coverUrl ?: return@LaunchedEffect
                val imageLoader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(coverUrl)
                    .build()
                val bitmap = imageLoader.execute(request).drawable?.toBitmap()
                bitmap ?: return@LaunchedEffect
                val palette =
                    Palette.from(bitmap.copy(Bitmap.Config.RGB_565, false)).generate()
                monetColor = Color(palette.getDominantColor(Color.Red.toArgb()))
                bitmap.recycle()
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