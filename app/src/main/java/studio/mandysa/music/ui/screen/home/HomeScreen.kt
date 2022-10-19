package studio.mandysa.music.ui.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.NavHost
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.screen.browse.BrowseScreen
import studio.mandysa.music.ui.screen.local.LocalScreen
import studio.mandysa.music.ui.screen.main.HomeBottomNavigationDestination

@Composable
fun HomeScreen(
    bottomNavController: NavController<HomeBottomNavigationDestination>,
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues
) {
    NavHost(bottomNavController) {
        when (it) {
            HomeBottomNavigationDestination.NeteaseCloud -> {
                BrowseScreen(
                    mainNavController,
                    dialogNavController,
                    paddingValues
                )
            }
            HomeBottomNavigationDestination.Local -> {
                LocalScreen(
                    mainNavController,
                    dialogNavController,
                    paddingValues
                )
            }
        }
    }
}