package studio.mandysa.music

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import studio.mandysa.music.ui.screen.SelectLoginScreen
import studio.mandysa.music.ui.screen.StartScreen
import studio.mandysa.music.ui.theme.MandySaMusicTheme
import studio.mandysa.music.ui.viewmodel.EventViewModel

class MainActivity : AppCompatActivity() {

    sealed class Screen(
        val route: String
    ) {
        object Start : Screen("start")
        object Login : Screen("login")
        object Main : Screen("main")
        object SelectLogin : Screen("select_login")
    }
    

    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MandySaMusicTheme {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                val navController = rememberNavController(bottomSheetNavigator)
                ModalBottomSheetLayout(bottomSheetNavigator) {
                    NavHost(navController = navController, startDestination = Screen.Start.route) {
                        composable(Screen.Start.route) {
                            StartScreen(navController)
                        }
                        composable(Screen.SelectLogin.route) {
                            SelectLoginScreen(this@MainActivity, navController)
                        }
                        composable(Screen.Main.route) {
                            MainScreen()
                        }
                        bottomSheet(Screen.Login.route) {
                            LoginScreen()
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