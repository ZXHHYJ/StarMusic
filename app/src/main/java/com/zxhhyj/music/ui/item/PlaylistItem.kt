package com.zxhhyj.music.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.ui.composable.AppIcon
import com.zxhhyj.music.ui.composable.AppAsyncImage
import com.zxhhyj.music.ui.theme.cardBackgroundColor
import com.zxhhyj.music.ui.theme.onBackground
import com.zxhhyj.music.ui.theme.roundShape
import com.zxhhyj.music.ui.theme.textColor



@Composable
fun PlaylistItem(imageVector: ImageVector, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(120.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(roundShape)
                .background(cardBackgroundColor)
                .clickable(onClick = onClick)
        ) {
            AppIcon(
                imageVector = imageVector,
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
        AppAsyncImage(modifier = Modifier.size(120.dp), url = coverUrl, onClick = onClick)
        Text(
            text = title,
            color = textColor,
            fontSize = 12.sp,
            maxLines = 2
        )
    }
}