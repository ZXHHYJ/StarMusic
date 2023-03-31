package studio.mandysa.music.ui.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import studio.mandysa.music.ui.theme.roundShape

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

@Composable
fun AppRoundCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color = contentColorFor(backgroundColor = backgroundColor),
    content: @Composable () -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    AppCard(
        modifier = modifier.onSizeChanged {
            size = it
        },
        shape = RoundedCornerShape(size.width),
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        content = content
    )
}