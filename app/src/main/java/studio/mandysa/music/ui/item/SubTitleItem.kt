package studio.mandysa.music.ui.item

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.theme.horizontal
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.vertical

@Composable
fun SubTitleItem(modifier: Modifier = Modifier, title: String) {
    Text(
        text = title,
        color = textColor,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontal, vertical = vertical)
    )
}