package studio.mandysa.music.ui.screen

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import studio.mandysa.music.MainActivity
import studio.mandysa.music.R
import studio.mandysa.music.ui.viewmodel.EventViewModel

@Composable
fun StartScreen(navController: NavHostController, viewModel: EventViewModel = viewModel()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = rememberImagePainter(R.mipmap.ic_launcher),
            contentDescription = null, modifier = Modifier.size(50.dp)
        )
    }
    val cookie = viewModel.getCookieLiveData().observeAsState()
    Handler(Looper.myLooper()!!).postDelayed(
        {
            navController.navigate(if (cookie.value != null) MainActivity.Screen.Main.route else MainActivity.Screen.SelectLogin.route) {
                launchSingleTop = true
            }
        }, 500
    )
}