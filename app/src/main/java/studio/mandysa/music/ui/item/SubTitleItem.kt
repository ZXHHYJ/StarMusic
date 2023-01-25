package studio.mandysa.music.ui.item

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.theme.defaultHorizontal
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.defaultVertical

@Composable
@Preview
fun SubTitleItem(title: String = "默认标题") {
    Text(
        text = title,
        color = textColor,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(horizontal = defaultHorizontal)
            .padding(top = 10.dp, bottom = defaultVertical)
    )
}