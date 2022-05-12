package studio.mandysa.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun PlaylistItem(title: String, coverUrl: String, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth =
        if (configuration.screenWidthDp.dp <= configuration.screenHeightDp.dp) configuration.screenWidthDp.dp else configuration.screenHeightDp.dp
    Column(
        modifier = Modifier
            .width(screenWidth / 3)
    ) {
        Card(
            elevation = 0.dp, shape = RoundedCornerShape(8.dp), modifier = Modifier
                .size(screenWidth / 3)
        ) {
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = onClick)
            )
        }
        Text(text = title, color = Color.Black, fontSize = 13.sp)
    }
}