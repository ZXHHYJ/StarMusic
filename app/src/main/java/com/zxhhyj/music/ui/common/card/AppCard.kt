package com.zxhhyj.music.ui.common.card

import androidx.compose.material.Card
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zxhhyj.music.ui.theme.roundShape

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    shape: Shape = roundShape,
    backgroundColor: Color,
    contentColor: Color = contentColorFor(backgroundColor = backgroundColor),
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        border = null,
        elevation = elevation,
        content = content
    )
}