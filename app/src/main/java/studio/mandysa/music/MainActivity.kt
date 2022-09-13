package studio.mandysa.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import studio.mandysa.music.logic.repository.UserRepository
import studio.mandysa.music.service.MediaPlayService
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.POPWindows
import studio.mandysa.music.ui.screen.login.LoginScreen
import studio.mandysa.music.ui.screen.main.MainScreen
import studio.mandysa.music.ui.theme.MandySaMusicTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            MandySaMusicTheme {
                val isLogin by UserRepository.isLoginLiveData.observeAsState()
                if (isLogin?.isNotEmpty() == true) {
                    MainScreen()
                } else
                    LoginScreen()
                POPWindows.PopWin()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //当处于暂停状态activity被销毁时连同服务一起销毁
        if (PlayManager.pause == true) {
            MediaPlayService.instance?.stopSelf()
        }
    }
}