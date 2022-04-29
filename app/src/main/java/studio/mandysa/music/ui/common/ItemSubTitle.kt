package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun ItemSubTitleScreen(title: String = "default title") {
    Text(
        text = title,
        color = Color.Black,
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 30.dp, bottom = 10.dp)
    )
}