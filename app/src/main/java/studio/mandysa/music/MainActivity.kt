package studio.mandysa.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import studio.mandysa.music.logic.user.UserManager
import studio.mandysa.music.ui.common.POPWindows
import studio.mandysa.music.ui.screen.login.LoginScreen
import studio.mandysa.music.ui.screen.main.MainScreen
import studio.mandysa.music.ui.theme.MandySaMusicTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            MandySaMusicTheme {
                val isLogin by UserManager.isLoginLiveData.observeAsState()
                if (isLogin?.isNotEmpty() == true) {
                    MainScreen()
                } else
                    LoginScreen()
                POPWindows.PopWin()
            }
        }
    }

}