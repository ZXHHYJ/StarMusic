package studio.mandysa.music.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import studio.mandysa.music.ui.theme.appAccentColor

@Composable
fun AppAsyncImage(
    modifier: Modifier = Modifier,
    url: String,
    onClick: (() -> Unit)? = null
) {
    AsyncImage(
        modifier = modifier.background(appAccentColor.copy(0.4f))
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