package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import studio.mandysa.music.ui.theme.containerColor
import studio.mandysa.music.ui.theme.roundedCornerShape

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        modifier = modifier,
        shape = roundedCornerShape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        content = content
    )
}

@Composable
fun AppRoundCard(modifier: Modifier = Modifier, content: @Composable (ColumnScope.() -> Unit)) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    Card(
        modifier = modifier.onSizeChanged {
            size = it
        },
        shape = RoundedCornerShape(size.width),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        content = content
    )
}