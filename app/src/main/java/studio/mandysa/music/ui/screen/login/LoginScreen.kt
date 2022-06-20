package studio.mandysa.music.ui.screen.login

import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.KenBurns
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.translucentWhite
import studio.mandysa.music.ui.theme.verticalMargin


@ExperimentalMaterial3Api
@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()) {
    val qrBitmap by loginViewModel.getQRBitmap().observeAsState(null)

    if (qrBitmap == null)
        loginViewModel.refresh()

    rememberSystemUiController().setSystemBarsColor(
        Color.Transparent,
        false,
        isNavigationBarContrastEnforced = false
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        KenBurns(
            modifier = Modifier.fillMaxSize(),
            imageUrl = "https://i.hexuexiao.cn/up/b1/c3/29/cc0ec73b1b1d8d4cbbfed2297d29c3b1.jpg",
            paused = false
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = verticalMargin, horizontal = horizontalMargin),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = Color.White
            )
            Card(
                modifier = Modifier.size(250.dp),
                colors = CardDefaults.cardColors(containerColor = translucentWhite),
            ) {
                if (qrBitmap != null) {
                    AndroidView(modifier = Modifier.fillMaxSize(), factory = { ImageView(it) }) {
                        it.setImageBitmap(qrBitmap)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier
                    .width(80.dp)
                    .height(40.dp),
                onClick = {
                    loginViewModel.refresh()
                },
                elevation = null,
                colors = buttonColors(backgroundColor = translucentWhite),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(text = "刷新")
            }
        }
    }
}