package studio.mandysa.music.ui.item

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.verticalMargin

@Composable
@Preview
fun ItemTitle(title: String = "默认标题") {
    Text(
        text = title,
        color = Color.Black,
        fontSize = 34.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(horizontal = horizontalMargin)
            .padding(top = 100.dp, bottom = verticalMargin)
    )
}