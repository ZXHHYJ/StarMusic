package studio.mandysa.music.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import studio.mandysa.music.ui.screen.play.PlayScreen
import studio.mandysa.music.ui.songmenu.SongMenu

/**
 * Happy 22nd Birthday Shuangshengzi
 */
sealed class MainScreenDestination(val route: String) {
    object Content : MainScreenDestination("content")
    object Play : MainScreenDestination("play")
    object SongMenu : MainScreenDestination("song_menu")
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun MainScreen() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(navController, startDestination = MainScreenDestination.Content.route) {
            composable(MainScreenDestination.Content.route) {
                ContentScreen(navController)
            }
            bottomSheet(MainScreenDestination.Play.route) {
                PlayScreen()
            }
            bottomSheet(MainScreenDestination.SongMenu.route + "/{id}") {
                SongMenu(id = it.arguments?.getString("id")!!)
            }
        }
    }
}