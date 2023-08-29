package com.zxhhyj.music.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCard

@Composable
fun AppAsyncImage(
    modifier: Modifier = Modifier,
    backgroundColor: Color = LocalColorScheme.current.highlight.copy(0.2f),
    data: Any?,
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = onClick?.let { Modifier.clickable(onClick = onClick) } ?: Modifier
    AppCard(backgroundColor = LocalColorScheme.current.background, modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .background(backgroundColor)
                .then(clickableModifier),
            model = ImageRequest.Builder(LocalContext.current)
                .data(data)
                .crossfade(if (SettingRepository.EnableLinkUI) 0 else 300)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}