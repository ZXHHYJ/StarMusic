package com.zxhhyj.music.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zxhhyj.ui.LocalColorScheme

@Composable
fun AppAsyncImage(
    modifier: Modifier = Modifier,
    data: Any?,
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = onClick?.let { Modifier.clickable(onClick = onClick) } ?: Modifier
    AsyncImage(
        modifier = modifier
            .background(Color.White)
            .background(LocalColorScheme.current.highlight.copy(0.2f))
            .then(clickableModifier),
        model = ImageRequest.Builder(LocalContext.current)
            .data(data)
            .crossfade(300)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}