package com.zxhhyj.music.ui.common.card

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AppRoundCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color = contentColorFor(backgroundColor = backgroundColor),
    content: @Composable () -> Unit
) {
    AppCard(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        content = content
    )
}