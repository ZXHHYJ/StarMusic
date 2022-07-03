package studio.mandysa.music.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.theme.textColor

@Composable
fun PlaylistItem(title: String, coverUrl: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(120.dp)
    ) {
        AppAsyncImage(size = 120.dp, url = coverUrl, onClick = onClick)
        Text(
            text = title,
            color = textColor,
            fontSize = 13.sp,
            maxLines = 2
        )
    }
}