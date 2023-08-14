package com.zxhhyj.ui.view

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zxhhyj.ui.theme.StarDimens

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(StarDimens.round),
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