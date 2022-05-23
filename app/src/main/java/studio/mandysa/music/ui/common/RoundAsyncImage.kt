package studio.mandysa.music.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import studio.mandysa.music.ui.theme.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundAsyncImage(size: Dp, url: String, onClick: () -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(round), modifier = Modifier
            .size(size)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
        )
    }
}