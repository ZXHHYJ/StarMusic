package studio.mandysa.music.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Contactless
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.map
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.olshevski.navigation.reimagined.*
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.screen.browse.BrowseScreen
import studio.mandysa.music.ui.screen.me.MeScreen
import studio.mandysa.music.ui.screen.search.SearchScreen
import studio.mandysa.music.ui.theme.indicatorColor
import studio.mandysa.music.ui.theme.navHeight
import studio.mandysa.music.ui.theme.neutralColor

enum class BottomNavigationDestination {
    Browse,
    Search,
    Me,
}

val BottomNavigationDestination.tabIcon
    get() = when (this) {
        BottomNavigationDestination.Browse -> Icons.Rounded.Contactless
        BottomNavigationDestination.Search -> Icons.Rounded.Search
        BottomNavigationDestination.Me -> Icons.Rounded.Person
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigationDrawer(
    modifier: Modifier,
    drawerContent: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    BoxWithConstraints {
        val bigScreen = maxWidth >= 600.dp
        Row(modifier = modifier) {
            Box(modifier.fillMaxHeight()) {
                if (bigScreen)
                    drawerContent.invoke()
            }
            Scaffold(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1.0f),
                content = content,
                bottomBar = {
                    Column {
                        if (!bigScreen)
                            bottomBar.invoke()
                        Box(
                            modifier = Modifier
                                .height(LocalDensity.current.run {
                                    WindowInsets.navigationBars
                                        .getBottom(this)
                                        .toDp()
                                })
                                .fillMaxWidth()
                                .background(neutralColor)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ContentScreen(mainNavController: NavController<MainScreenDestination>) {
    rememberSystemUiController().setSystemBarsColor(
        Color.Transparent,
        true,
        isNavigationBarContrastEnforced = false
    )
    val navController = rememberNavController(startDestination = BottomNavigationDestination.Browse)
    AppNavigationDrawer(
        modifier = Modifier.statusBarsPadding(),
        drawerContent = {
            NavigationRail {

            }
        },
        content = { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(navController) {
                    when (it) {
                        BottomNavigationDestination.Browse -> {
                            BrowseScreen()
                        }
                        BottomNavigationDestination.Search -> {
                            SearchScreen()
                        }
                        BottomNavigationDestination.Me -> {
                            MeScreen()
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .align(Alignment.BottomCenter)
                ) {
                    val isVisible by PlayManager.changeMusicLiveData().map {
                        return@map true
                    }.observeAsState(false)
                    if (isVisible)
                        ControllerScreen {
                            mainNavController.navigate(MainScreenDestination.Play)
                        }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(navHeight),
                containerColor = neutralColor,
                contentColor = Color.White
            ) {
                val lastDestination = navController.backstack.entries.last().destination
                BottomNavigationDestination.values().forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                screen.tabIcon,
                                contentDescription = null
                            )
                        }, colors = NavigationBarItemDefaults.colors(
                            indicatorColor = indicatorColor
                        ),
                        selected = screen == lastDestination,
                        onClick = {
                            if (!navController.moveToTop { it == screen }) {
                                // if there is no existing instance, add it
                                navController.navigate(screen)
                            }
                        }
                    )
                }
            }
        }
    )

}