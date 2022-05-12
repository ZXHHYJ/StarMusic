package studio.mandysa.music

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.yanzhenjie.sofia.Sofia
import studio.mandysa.music.ui.screen.LoginScreen
import studio.mandysa.music.ui.screen.MainScreen
import studio.mandysa.music.ui.screen.StartScreen
import studio.mandysa.music.ui.theme.MandySaMusicTheme
import studio.mandysa.music.ui.viewmodel.EventViewModel

class MainActivity : AppCompatActivity() {

    sealed class Screen(
        val route: String
    ) {
        object Login : Screen("login")
        object Main : Screen("main")
        object Start : Screen("start")
    }

    private val mEvent by viewModels<EventViewModel>()

    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MandySaMusicTheme {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                val navController = rememberNavController(bottomSheetNavigator)
                ModalBottomSheetLayout(bottomSheetNavigator) {
                    val cookie = mEvent.getCookieLiveData().observeAsState()
                    NavHost(
                        navController = navController,
                        startDestination = if (cookie.value != null) Screen.Main.route else Screen.Start.route
                    ) {
                        composable(Screen.Main.route) {
                            MainScreen()
                        }
                        composable(Screen.Start.route) {
                            StartScreen(this@MainActivity, navController)
                        }
                        bottomSheet(Screen.Login.route) {
                            LoginScreen(navController)
                        }
                    }
                }
            }
        }
        Sofia.with(this).apply {
            invasionStatusBar().invasionNavigationBar()
            statusBarDarkFont().navigationBarDarkFont()
        }
    }

}