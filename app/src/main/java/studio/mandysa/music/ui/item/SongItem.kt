package studio.mandysa.music.ui.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.R
import studio.mandysa.music.ui.theme.textColorLight

@Composable
fun SongItem(position: Int, title: String, singer: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = "$position",
            fontSize = 16.sp,
            color = textColorLight,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp)
                .size(50.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
        ) {
            Text(
                text = title,
                color = Color.Black,
                fontSize = 15.sp,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = singer,
                color = textColorLight,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 5.dp)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_round_more_vert_24),
            contentDescription = null, modifier = Modifier.padding(end = 16.dp)
        )
    }
}