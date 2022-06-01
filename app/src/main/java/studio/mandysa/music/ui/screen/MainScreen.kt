package studio.mandysa.music.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Contactless
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import studio.mandysa.music.ui.screen.browse.BrowseScreen
import studio.mandysa.music.ui.screen.me.MeScreen
import studio.mandysa.music.ui.theme.navHeight

/**
 * Happy 22nd Birthday Shuangshengzi
 */

private sealed class MainNavScreen(
    val route: String,
    val vector: ImageVector
) {
    object Browse : MainNavScreen("browse", Icons.Rounded.Contactless)
    object Me : MainNavScreen("me", Icons.Rounded.Person)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        MainNavScreen.Browse,
        MainNavScreen.Me,
    )
    rememberSystemUiController().apply {
        setSystemBarsColor(Color.Transparent, true, isNavigationBarContrastEnforced = false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        NavHost(
            navController,
            startDestination = MainNavScreen.Browse.route,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .statusBarsPadding()
        ) {
            composable(MainNavScreen.Browse.route) { BrowseScreen() }
            composable(MainNavScreen.Me.route) { MeScreen() }
        }
        ControllerScreen()
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(navHeight)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            items.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            screen.vector,
                            contentDescription = null
                        )
                    },
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
    }
}