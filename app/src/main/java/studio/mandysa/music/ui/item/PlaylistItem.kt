package studio.mandysa.music.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.theme.containerColor
import studio.mandysa.music.ui.theme.roundedCornerShape
import studio.mandysa.music.ui.theme.onBackground
import studio.mandysa.music.ui.theme.textColor

@Composable
fun PlaylistItem(icon: ImageVector, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(120.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(roundedCornerShape)
                .background(containerColor)
                .clickable(onClick = onClick)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp), tint = onBackground
            )
        }
    }
}

@Composable
fun PlaylistItem(title: String, coverUrl: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(120.dp)
    ) {
        AppAsyncImage(size = 120.dp, url = coverUrl, onClick = onClick)
        Text(
            text = title,
            color = textColor,
            fontSize = 12.sp,
            maxLines = 2
        )
    }
}