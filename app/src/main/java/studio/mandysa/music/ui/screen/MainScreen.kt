package studio.mandysa.music.ui.screen

import android.view.Gravity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
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
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.theme.navHeight

private sealed class Screen(
    val route: String,
    @DrawableRes val resourceId: Int
) {
    object Browse : Screen("browse", R.drawable.ic_round_contactless_24)
    object Me : Screen("me", R.drawable.ic_round_person_24)
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
                SlidingPanel(modifier = Modifier,
                    gravity = Gravity.BOTTOM,
                    panelHeight = with(LocalDensity.current) {
                        WindowInsets.navigationBars.getBottom(
                            LocalDensity.current
                        ) + navHeight.roundToPx() * if (PlayManager.changePlayListLiveData()
                                .observeAsState().value?.isEmpty() != false
                        ) 1 else 2
                    },
                    shadowHeight = with(LocalDensity.current) { 2.dp.roundToPx() },
                    content = {
                        NavHost(
                            navController,
                            startDestination = Screen.Browse.route,
                            modifier = Modifier
                                .fillMaxSize()
                                .statusBarsPadding()
                        ) {
                            composable(Screen.Browse.route) { BrowseScreen() }
                            composable(Screen.Me.route) { }
                        }
                    },
                    panel = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopStart
                        ) {
                            ControllerScreen()
                        }
                    })
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(with(LocalDensity.current) {
                            WindowInsets.navigationBars
                                .getBottom(
                                    LocalDensity.current
                                )
                                .toDp() + navHeight
                        })
                        .align(Alignment.BottomCenter),
                    containerColor = Color.White
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    ImageVector.vectorResource(screen.resourceId),
                                    contentDescription = null
                                )
                            },
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