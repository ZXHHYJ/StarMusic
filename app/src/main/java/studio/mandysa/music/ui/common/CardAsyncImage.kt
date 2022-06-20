package studio.mandysa.music.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import studio.mandysa.music.ui.theme.cornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardAsyncImage(
    shape: Shape = cornerShape,
    size: Dp,
    url: String,
    onClick: (() -> Unit)? = null
) {
    Card(
        shape = shape,
        modifier = Modifier
            .size(size)
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