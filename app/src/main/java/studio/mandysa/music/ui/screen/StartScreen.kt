package studio.mandysa.music.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.MultiBottomSheet

@Composable
fun StartScreen(openSheet: (MultiBottomSheet.Intent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        AsyncImage(
            model = "https://static-ali.ihansen.org/app/bg1440/9b9d6yKkago.jpg!o",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        )
        Row(modifier = Modifier
            .navigationBarsPadding()) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                onClick = { openSheet.invoke(MultiBottomSheet.Intent()) }) {
                Text(text = stringResource(id = R.string.login), color = Color.Black)
            }
            Button(modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f), onClick = { }) {
                Text(text = stringResource(id = R.string.exit), color = Color.Black)
            }
        }
    }
}