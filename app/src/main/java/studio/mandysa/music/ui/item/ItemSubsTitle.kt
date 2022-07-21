package studio.mandysa.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.remindTextColor
import studio.mandysa.music.ui.theme.verticalMargin

@Composable
fun ItemSubsTitle(title: String = "默认标题", sub: String = "默认副标题", subOnClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = horizontalMargin)
            .padding(top = 25.dp, bottom = verticalMargin)
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Text(
            modifier = Modifier.clickable(onClick = subOnClick),
            text = sub,
            color = remindTextColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}