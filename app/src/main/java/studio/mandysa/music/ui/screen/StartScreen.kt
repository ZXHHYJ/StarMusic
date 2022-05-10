package studio.mandysa.music.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.MultiBottomSheet
import studio.mandysa.music.ui.theme.horizontalMargin

@Composable
fun StartScreen(openSheet: (MultiBottomSheet.Intent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Red,
                        Color.Blue
                    )
                ),
                alpha = 0.5f
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = horizontalMargin),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.login_to),
                fontSize = 34.sp,
                color = Color.White
            )
            Text(text = stringResource(R.string.app_name), fontSize = 34.sp)
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = { openSheet.invoke(MultiBottomSheet.Intent()) }) {
            Text(text = stringResource(id = R.string.login), color = Color.Black)
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
            Text(text = stringResource(id = R.string.exit), color = Color.Black)
        }
    }
}