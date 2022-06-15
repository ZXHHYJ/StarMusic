package studio.mandysa.music.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Contactless
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
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
import studio.mandysa.music.ui.theme.background
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

@Composable
fun ContentScreen(mainNavController: NavController) {
    val navController = rememberNavController()
    rememberSystemUiController().apply {
        setSystemBarsColor(Color.Transparent, true, isNavigationBarContrastEnforced = false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .statusBarsPadding()
        ) {
            NavHost(
                navController,
                startDestination = ContentScreenDestination.Browse.route
            ) {
                composable(ContentScreenDestination.Browse.route) { BrowseScreen() }
                composable(ContentScreenDestination.Search.route) { SearchScreen() }
                composable(ContentScreenDestination.Me.route) { MeScreen() }
            }
            val isVisible by PlayManager.changeMusicLiveData().map {
                return@map true
            }.observeAsState(false)
            if (isVisible) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) {
                    ControllerScreen { mainNavController.navigate(MainScreenDestination.Play.route) }
                }
            }
        }
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