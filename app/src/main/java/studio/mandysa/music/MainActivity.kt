package studio.mandysa.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import studio.mandysa.music.ui.screen.MainScreen
import studio.mandysa.music.ui.screen.StartScreen
import studio.mandysa.music.ui.theme.MandySaMusicTheme
import studio.mandysa.music.ui.viewmodel.EventViewModel

class MainActivity : ComponentActivity() {

    sealed class NavScreen(
        val route: String
    ) {
        object Main : NavScreen("main")
        object Start : NavScreen("start")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            MandySaMusicTheme {
                val event: EventViewModel = viewModel()
                val navController = rememberNavController()
                val cookie = event.getCookieLiveData().observeAsState()
                NavHost(
                    navController = navController,
                    startDestination = if (cookie.value != null) NavScreen.Main.route else NavScreen.Start.route
                ) {
                    composable(NavScreen.Main.route) {
                        MainScreen()
                    }
                    composable(NavScreen.Start.route) {
                        StartScreen(navController)
                    }
                }
            }
        }
    }

}