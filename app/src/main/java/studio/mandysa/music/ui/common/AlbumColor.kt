package studio.mandysa.music.ui.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import coil.ImageLoader
import coil.request.ImageRequest


@Composable
fun AlbumColor(modifier: Modifier, url: String) {
    val context = LocalContext.current
    var colorBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }
    LaunchedEffect(url) {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        val bitmap = imageLoader.execute(request).drawable?.toBitmap()
        if (bitmap != null) {
            colorBitmap = bitmap.scale(1, 1).asImageBitmap()
        }
    }
    colorBitmap?.let {
        Image(
            bitmap = it,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = modifier
        )
    }
}