package studio.mandysa.music.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import studio.mandysa.music.MainActivity
import studio.mandysa.music.R
import studio.mandysa.music.ui.theme.Blue40

@Composable
fun SelectLoginScreen(activity: MainActivity, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        AsyncImage(
            model = "https://i.hexuexiao.cn/up/b1/c3/29/cc0ec73b1b1d8d4cbbfed2297d29c3b1.jpg",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        )
        Row {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .background(Blue40)
                    .navigationBarsPadding(),
                onClick = { navController.navigate(MainActivity.Screen.SelectLogin.route) }) {
                Text(text = stringResource(id = R.string.login), color = Color.White)
            }
            TextButton(modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .navigationBarsPadding(), onClick = { activity.finish() }) {
                Text(text = stringResource(id = R.string.exit), color = Color.Black)
            }
        }
    }
}