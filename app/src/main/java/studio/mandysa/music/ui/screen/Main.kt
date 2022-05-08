package studio.mandysa.music.ui.screen

import android.view.Gravity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sothree.slidinguppanel.ktx.SlidingPanel
import studio.mandysa.music.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val iconId: Int
) {
    object Browse : Screen("browse", R.string.browse, R.drawable.ic_round_contactless_24)
    object Me : Screen("me", R.string.me, R.drawable.ic_round_person_24)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Browse,
        Screen.Me,
    )
    BoxWithConstraints {
        if (maxWidth >= 600.dp) {
            Row {

            }
        } else {
            Box {
                SlidingPanel(modifier = Modifier
                    .statusBarsPadding(),
                    gravity = Gravity.BOTTOM,
                    panelHeight = LocalDensity.current.run { 56.dp.roundToPx() },
                    shadowHeight = LocalDensity.current.run { 5.dp.roundToPx() },
                    content = {
                        NavHost(
                            navController,
                            startDestination = Screen.Browse.route
                        ) {
                            composable(Screen.Browse.route) { BrowseScreen() }
                            composable(Screen.Me.route) { }
                        }
                    },
                    panel = {
                        ControllerScreen()
                    })
                BottomNavigation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .align(Alignment.BottomCenter), backgroundColor = Color.White
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    ImageVector.vectorResource(screen.iconId),
                                    contentDescription = null
                                )
                            },
                            label = { Text(stringResource(screen.resourceId)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}