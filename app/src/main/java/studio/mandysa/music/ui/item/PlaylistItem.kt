package studio.mandysa.music.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.common.CardAsyncImage
import studio.mandysa.music.ui.theme.horizontalMargin

@Composable
fun PlaylistItem(title: String, coverUrl: String, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth =
        if (configuration.screenWidthDp.dp <= configuration.screenHeightDp.dp) configuration.screenWidthDp.dp else configuration.screenHeightDp.dp
    val width = (screenWidth - horizontalMargin * 3) / 3
    Column(
        modifier = Modifier
            .width(width)
    ) {
        CardAsyncImage(size = width, url = coverUrl, onClick = onClick)
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 13.sp,
            maxLines = 2
        )
    }
}