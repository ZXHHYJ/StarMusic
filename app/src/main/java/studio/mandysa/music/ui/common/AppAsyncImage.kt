package studio.mandysa.music.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import studio.mandysa.music.ui.theme.emptyImageBackground
import studio.mandysa.music.ui.theme.smallRound

@Composable
fun AppRoundAsyncImage(
    modifier: Modifier = Modifier,
    emptyBackground: Color = emptyImageBackground,
    url: String,
    onClick: (() -> Unit)? = null
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    AppAsyncImage(
        modifier = modifier.onSizeChanged {
            size = it
        },
        emptyBackground = emptyBackground, cornerSize = size.height.dp / 2,
        url = url,
        onClick = onClick
    )
}

@Composable
fun AppAsyncImage(
    modifier: Modifier = Modifier,
    emptyBackground: Color = emptyImageBackground,
    cornerSize: Dp = smallRound,
    url: String,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerSize))
            .background(color = emptyBackground)
    ) {
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