package studio.mandysa.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import studio.mandysa.music.ui.common.POPWindows
import studio.mandysa.music.ui.screen.main.MainScreen
import studio.mandysa.music.ui.theme.MandySaMusicTheme

class MainActivity  : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            MandySaMusicTheme {
                MainScreen()
                POPWindows.PopWin()
            }
        }
    }

}