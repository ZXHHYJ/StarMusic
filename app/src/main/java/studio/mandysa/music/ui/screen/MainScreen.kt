package studio.mandysa.music.ui.screen

import androidx.compose.runtime.Composable
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.rememberNavController
import studio.mandysa.music.ui.screen.play.PlayScreen

/**
 * Happy 22nd Birthday Shuangshengzi
 */

enum class MainScreenDestination {
    Content,
    Play,
}

@Composable
fun MainScreen() {
    val navController = rememberNavController(startDestination = MainScreenDestination.Content)
    NavBackHandler(navController)
    NavHost(navController) {
        when (it) {
            MainScreenDestination.Content -> {
                ContentScreen(navController)
            }
            MainScreenDestination.Play -> {
                PlayScreen()
            }
        }
        /* composable(MainScreenDestination.Content.route) {
             ContentScreen(navController)
         }
         composable(MainScreenDestination.Play.route) {

         }
         bottomSheet(MainScreenDestination.SongMenu.route + "/{id}") {
             SongMenu(navController, id = it.arguments?.getString("id")!!)
         }*/
    }
}