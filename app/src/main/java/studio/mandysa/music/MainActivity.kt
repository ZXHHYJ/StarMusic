package studio.mandysa.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import studio.mandysa.music.ui.screen.LoginScreen
import studio.mandysa.music.ui.screen.MainScreen
import studio.mandysa.music.ui.screen.StartScreen
import studio.mandysa.music.ui.theme.MandySaMusicTheme
import studio.mandysa.music.ui.viewmodel.EventViewModel

class MainActivity : ComponentActivity() {

    sealed class NavScreen(
        val route: String
    ) {
        object Login : NavScreen("login")
        object Main : NavScreen("main")
        object Start : NavScreen("start")
    }

    private val mEvent by viewModels<EventViewModel>()

    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            MandySaMusicTheme {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                val navController = rememberNavController(bottomSheetNavigator)
                ModalBottomSheetLayout(bottomSheetNavigator) {
                    val cookie = mEvent.getCookieLiveData().observeAsState()
                    NavHost(
                        navController = navController,
                        startDestination = if (cookie.value != null) NavScreen.Main.route else NavScreen.Start.route
                    ) {
                        composable(NavScreen.Main.route) {
                            MainScreen()
                        }
                        composable(NavScreen.Start.route) {
                            StartScreen(this@MainActivity, navController)
                        }
                        bottomSheet(NavScreen.Login.route) {
                            LoginScreen(navController)
                        }
                    }
                }
            }
        }
    }

}