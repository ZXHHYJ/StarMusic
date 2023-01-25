package studio.mandysa.music.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import studio.mandysa.music.ui.theme.containerColor
import studio.mandysa.music.ui.theme.defaultHorizontal
import studio.mandysa.music.ui.theme.defaultVertical

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    click: () -> Unit = {},
    rowScope: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = defaultHorizontal, vertical = defaultVertical)
            .height(48.dp)
            .clip(CircleShape)
            .background(containerColor)
            .clickable(onClick = click)
            .padding(horizontal = defaultHorizontal),
        horizontalArrangement = Arrangement.spacedBy(defaultHorizontal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        rowScope.invoke(this)
    }
}