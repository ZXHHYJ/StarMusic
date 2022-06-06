package studio.mandysa.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import studio.mandysa.music.logic.user.UserManager
import studio.mandysa.music.ui.screen.MainScreen
import studio.mandysa.music.ui.screen.login.LoginScreen
import studio.mandysa.music.ui.theme.MandySaMusicTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            MandySaMusicTheme {
                val cookie = UserManager.getCookieLiveData().observeAsState()
                if (cookie.value != null) {
                    MainScreen()
                } else
                    LoginScreen()
            }
        }
    }

}