package studio.mandysa.music.ui.common

import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import studio.mandysa.music.ui.theme.containerColor
import studio.mandysa.music.ui.theme.roundedCornerShape

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = roundedCornerShape,
        backgroundColor = containerColor,
        elevation = 0.dp,
        content = content
    )
}