package studio.mandysa.music.ui.screen

import android.view.Gravity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Contactless
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.map
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sothree.slidinguppanel.PanelState
import com.sothree.slidinguppanel.ktx.SlidingPanel
import studio.mandysa.music.service.playmanager.PlayManager
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
    var panelState by rememberSaveable {
        mutableStateOf(PanelState.COLLAPSED)
    }
    rememberSystemUiController().apply {
        setSystemBarsColor(
            Color.Transparent,
            panelState != PanelState.EXPANDED,
            isNavigationBarContrastEnforced = false
        )
    }
    val showPanel by PlayManager.changeMusicLiveData().map {
        return@map true
    }.observeAsState(false)
    var alpha by rememberSaveable {
        mutableStateOf(0f)
    }
    BackHandler(enabled = panelState == PanelState.EXPANDED) {
        panelState = PanelState.COLLAPSED
    }
    BoxWithConstraints(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        if (maxWidth >= 600.dp) {
            Row {

            }
        } else {
            Box {
                val navigationBarHeight = WindowInsets.navigationBars
                SlidingPanel(modifier = Modifier,
                    gravity = Gravity.BOTTOM,
                    panelHeight = {
                        navigationBarHeight.getBottom(this) + navHeight.roundToPx() * if (showPanel) 2 else 1
                    },
                    enabledScrollable = true,
                    panelState = panelState,
                    panelStateChange = { state, slideOffset ->
                        when (state) {
                            PanelState.DRAGGING -> {
                                alpha = slideOffset * 40
                            }
                            else -> {
                                panelState = state
                            }
                        }
                    },
                    content = {
                        NavHost(
                            navController,
                            startDestination = MainNavScreen.Browse.route,
                            modifier = Modifier
                                .fillMaxSize()
                                .statusBarsPadding()
                        ) {
                            composable(MainNavScreen.Browse.route) { BrowseScreen() }
                            composable(MainNavScreen.Me.route) { MeScreen() }
                        }
                    },
                    panel = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Box(modifier = Modifier.alpha(alpha)) {
                                PlayScreen()
                            }
                            Box(
                                modifier = Modifier
                                    .alpha(1 - alpha)
                                    .clickable { panelState = PanelState.EXPANDED }) {
                                ControllerScreen()
                            }
                        }
                    })
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = navHeight * alpha / 10)
                        .align(Alignment.BottomCenter)
                        .height(LocalDensity.current.run {
                            navHeight + navigationBarHeight
                                .getBottom(
                                    this
                                )
                                .toDp()
                        })
                ) {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxSize()
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
        }
    }
}