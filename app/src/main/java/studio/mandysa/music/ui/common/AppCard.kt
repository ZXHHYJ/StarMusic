package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import studio.mandysa.music.ui.theme.containerColor
import studio.mandysa.music.ui.theme.roundedCornerShape

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        modifier,
        roundedCornerShape,
        CardDefaults.cardColors(containerColor = containerColor),
        CardDefaults.cardElevation(),
        null,
        content
    )
}