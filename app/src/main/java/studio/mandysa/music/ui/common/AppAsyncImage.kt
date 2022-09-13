package studio.mandysa.music.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import studio.mandysa.music.ui.theme.emptyImageBackground
import studio.mandysa.music.ui.theme.defaultRound

@Composable
fun AppAsyncImage(
    modifier: Modifier = Modifier,
    cornerSize: Dp = defaultRound,
    url: String?,
    onClick: (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(cornerSize),
        modifier = modifier,
        backgroundColor = emptyImageBackground,
        elevation = 0.dp
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