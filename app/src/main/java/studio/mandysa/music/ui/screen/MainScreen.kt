package studio.mandysa.music.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import studio.mandysa.music.ui.screen.play.PlayScreen

/**
 * Happy 22nd Birthday Shuangshengzi
 */

sealed class MainScreenDestination(val route: String) {
    object Content : MainScreenDestination("content")
    object Play : MainScreenDestination("play")
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = MainScreenDestination.Content.route) {
        composable(MainScreenDestination.Content.route) {
            ContentScreen(navController)
        }
        composable(MainScreenDestination.Play.route) {
            PlayScreen()
        }
    }
}