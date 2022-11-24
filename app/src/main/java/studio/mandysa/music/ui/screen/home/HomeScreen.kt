package studio.mandysa.music.ui.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.screen.main.HomeBottomNavigationDestination

@Composable
fun HomeScreen(
    bottomNavController: NavController<HomeBottomNavigationDestination>,
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues
) {
    //是否启用在线音乐

}