package studio.mandysa.music.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.theme.contentColor
import studio.mandysa.music.ui.theme.onBackground

@Composable
fun RoundIconItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick, modifier = Modifier
                .padding(5.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(contentColor)
        ) {
            Icon(
                icon, null,
                tint = onBackground
            )
        }
        Text(text = title, color = onBackground, fontSize = 12.sp)
    }
}