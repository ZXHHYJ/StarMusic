package studio.mandysa.music.ui.item

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.verticalMargin

@Composable
@Preview
fun ItemSubTitle(title: String = "默认标题") {
    Text(
        text = title,
        color = textColor,
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(horizontal = horizontalMargin)
            .padding(top = 25.dp, bottom = verticalMargin)
    )
}