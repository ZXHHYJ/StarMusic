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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.map
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.screen.browse.BrowseScreen
import studio.mandysa.music.ui.screen.me.MeScreen
import studio.mandysa.music.ui.screen.search.SearchScreen
import studio.mandysa.music.ui.theme.indicatorColor
import studio.mandysa.music.ui.theme.navHeight
import studio.mandysa.music.ui.theme.neutralColor

private sealed class ContentScreenDestination(
    val route: String,
    val vector: ImageVector
) {
    object Browse : ContentScreenDestination("browse", Icons.Rounded.Contactless)
    object Search : ContentScreenDestination("search", Icons.Rounded.Search)
    object Me : ContentScreenDestination("me", Icons.Rounded.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(mainNavController: NavController) {
    rememberSystemUiController().setSystemBarsColor(
        Color.Transparent,
        true,
        isNavigationBarContrastEnforced = false
    )
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        content = { padding ->
            NavHost(
                navController,
                startDestination = ContentScreenDestination.Browse.route
            ) {
                composable(ContentScreenDestination.Browse.route) {
                    BrowseScreen(
                        mainNavController,
                        paddingValues = padding
                    )
                }
                composable(ContentScreenDestination.Search.route) { SearchScreen(paddingValues = padding) }
                composable(ContentScreenDestination.Me.route) { MeScreen(paddingValues = padding) }
            }
        },
        bottomBar = {
            Column {
                val isVisible by PlayManager.changeMusicLiveData().map {
                    return@map true
                }.observeAsState(false)
                if (isVisible)
                    ControllerScreen { mainNavController.navigate(MainScreenDestination.Play.route) }
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(navHeight),
                    containerColor = neutralColor,
                    contentColor = Color.White
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    listOf(
                        ContentScreenDestination.Browse,
                        ContentScreenDestination.Search,
                        ContentScreenDestination.Me,
                    ).forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    screen.vector,
                                    contentDescription = null
                                )
                            }, colors = NavigationBarItemDefaults.colors(
                                indicatorColor = indicatorColor
                            ),
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
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