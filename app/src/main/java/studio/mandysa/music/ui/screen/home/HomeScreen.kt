package studio.mandysa.music.ui.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.NavHost
import studio.mandysa.music.logic.repository.UserRepository
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
    //是否启用在线音乐
    val enableNeteaseCloud by UserRepository.isLoginLiveData.observeAsState()
    if (enableNeteaseCloud == true) {
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
    } else {
        LocalScreen(
            mainNavController,
            dialogNavController,
            paddingValues
        )
    }
}