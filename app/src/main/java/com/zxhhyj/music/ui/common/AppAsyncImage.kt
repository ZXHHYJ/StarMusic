package com.zxhhyj.music.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.view.AppCard

@Composable
fun AppAsyncImage(
    modifier: Modifier = Modifier,
    data: Any?,
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = onClick?.let { Modifier.clickable(onClick = onClick) } ?: Modifier
    AppCard(backgroundColor = Color.Transparent, modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .then(clickableModifier),
            model = ImageRequest.Builder(LocalContext.current)
                .data(data)
                .placeholder(R.drawable.nothing_cover)
                .error(R.drawable.nothing_cover)
                .crossfade(if (SettingRepository.EnableLinkUI) 0 else 300)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}