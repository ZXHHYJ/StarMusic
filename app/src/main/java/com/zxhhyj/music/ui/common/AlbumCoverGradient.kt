package com.zxhhyj.music.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import com.zxhhyj.music.logic.utils.BitmapUtils

@Composable
fun AlbumCoverGradient(
    modifier: Modifier,
    url: String?,
) {
    var keyColor by remember {
        mutableStateOf(Color.LightGray)
    }
    var imageBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }
    val context = LocalContext.current
    val imageLoader = ImageLoader(context)
    LaunchedEffect(url) {
        if (url == null) return@LaunchedEffect
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        val bitmap = imageLoader.execute(request).drawable?.toBitmap() ?: return@LaunchedEffect
        val palette = Palette.from(BitmapUtils.compressBitmap(bitmap)).generate()
        runCatching {
            keyColor = Color(palette.dominantSwatch!!.rgb)
        }
        imageBitmap = bitmap.asImageBitmap()
    }
    BoxWithConstraints(modifier = modifier.background(keyColor)) {
        Image(
            bitmap = imageBitmap ?: return@BoxWithConstraints,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(this@BoxWithConstraints.maxWidth)
                .drawWithContent {
                    drawContent()
                    val gradient = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            keyColor
                        ),
                        startY = 0f,
                        endY = size.height
                    )
                    drawRect(brush = gradient, size = size)
                }
        )
    }
}