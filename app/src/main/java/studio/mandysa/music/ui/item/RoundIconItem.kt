package studio.mandysa.music.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.theme.containerColor
import studio.mandysa.music.ui.theme.onBackground
import studio.mandysa.music.ui.theme.textColor

@Composable
fun RoundIconItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(40.dp))
                .background(containerColor)
                .clickable {
                    onClick.invoke()
                }
        ) {
            Icon(
                icon,
                null,
                tint = onBackground,
                modifier = Modifier.padding(14.dp)
            )
        }
        Text(text = title, color = textColor, fontSize = 12.sp)
    }
}