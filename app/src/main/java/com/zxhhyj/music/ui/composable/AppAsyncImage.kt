package com.zxhhyj.music.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zxhhyj.music.ui.theme.appAccentColor

@Composable
fun AppAsyncImage(
    modifier: Modifier = Modifier,
    url: String?,
    onClick: (() -> Unit)? = null
) {
    AsyncImage(
        modifier = modifier
            .background(Color.White)
            .background(appAccentColor.copy(0.2f))
            .run {
                onClick?.run { clickable(onClick = onClick) } ?: this
            },
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(300)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}