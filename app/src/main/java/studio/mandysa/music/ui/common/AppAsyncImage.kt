package studio.mandysa.music.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import studio.mandysa.music.ui.theme.emptyImageBackground

@Composable
fun AppAsyncImage(
    modifier: Modifier = Modifier,
    emptyBackground: Color = emptyImageBackground,
    url: String,
    onClick: (() -> Unit)? = null
) {
    Box(modifier = modifier.background(color = emptyBackground)) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .run {
                    if (onClick != null)
                        clickable(onClick = onClick)
                    else this
                },
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}