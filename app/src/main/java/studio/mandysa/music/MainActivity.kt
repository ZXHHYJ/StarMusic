package studio.mandysa.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import studio.mandysa.music.ui.screen.MainScreen
import studio.mandysa.music.ui.screen.LoginScreen
import studio.mandysa.music.ui.theme.MandySaMusicTheme
import studio.mandysa.music.ui.viewmodel.EventViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            MandySaMusicTheme {
                val event: EventViewModel = viewModel()
                val cookie = event.getCookieLiveData().observeAsState()
                if (cookie.value != null) {
                    MainScreen()
                } else
                    LoginScreen()
            }
        }
    }

}