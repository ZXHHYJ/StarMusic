package studio.mandysa.music.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.map
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.olshevski.navigation.reimagined.*
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.PanelState
import studio.mandysa.music.ui.common.SlidingPanel
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.screen.browse.BrowseScreen
import studio.mandysa.music.ui.screen.controller.ControllerScreen
import studio.mandysa.music.ui.screen.me.AboutScreen
import studio.mandysa.music.ui.screen.me.MeScreen
import studio.mandysa.music.ui.screen.me.SettingScreen
import studio.mandysa.music.ui.screen.me.like.LikeScreen
import studio.mandysa.music.ui.screen.play.PlayScreen
import studio.mandysa.music.ui.screen.playlist.PlaylistScreen
import studio.mandysa.music.ui.screen.search.SearchScreen
import studio.mandysa.music.ui.theme.indicatorColor
import studio.mandysa.music.ui.theme.navHeight
import studio.mandysa.music.ui.theme.neutralColor

/**
 * Happy 22nd Birthday Shuangshengzi
 */
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
    controllerBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
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
                        controllerBar.invoke()
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
fun MainScreen() {
    val navController =
        rememberNavController<ScreenDestination>(startDestination = ScreenDestination.Main)
    val bottomNavController =
        rememberNavController(startDestination = BottomNavigationDestination.Browse)
    var panelState by rememberSaveable {
        mutableStateOf(PanelState.COLLAPSED)
    }
    rememberSystemUiController().setSystemBarsColor(
        Color.Transparent,
        panelState != PanelState.EXPANDED,
        isNavigationBarContrastEnforced = false
    )
    SlidingPanel(
        panelHeight = 0.dp,
        panelStateChange = {
            panelState = it
        },
        content = {
            AppNavigationDrawer(
                modifier = Modifier.statusBarsPadding(),
                drawerContent = {
                    NavigationRail {

                    }
                },
                controllerBar = {
                    val isVisible by PlayManager.changeMusicLiveData().map {
                        return@map true
                    }.observeAsState(false)
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        ControllerScreen {
                            it.invoke(PanelState.EXPANDED)
                        }
                    }
                },
                bottomBar = {
                    AnimatedVisibility(
                        visible = navController.backstack.entries.size <= 1,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        NavigationBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(navHeight),
                            containerColor = neutralColor,
                            contentColor = Color.White
                        ) {
                            val bottomLastDestination =
                                bottomNavController.backstack.entries.last().destination
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
                                    selected = screen == bottomLastDestination,
                                    onClick = {
                                        if (!bottomNavController.moveToTop { it == screen }) {
                                            bottomNavController.navigate(screen)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            ) { padding ->
                if (panelState != PanelState.EXPANDED)
                    NavBackHandler(navController)
                //面板展开时不再处理这里的导航
                NavHost(navController) { screenDestination ->
                    when (screenDestination) {
                        ScreenDestination.Main -> {
                            NavHost(bottomNavController) {
                                when (it) {
                                    BottomNavigationDestination.Browse -> {
                                        BrowseScreen(navController)
                                    }
                                    BottomNavigationDestination.Search -> {
                                        SearchScreen(navController)
                                    }
                                    BottomNavigationDestination.Me -> {
                                        MeScreen(navController)
                                    }
                                }
                            }
                        }
                        ScreenDestination.About -> {
                            AboutScreen()
                        }
                        is ScreenDestination.Like -> {
                            LikeScreen(navController = navController, id = screenDestination.id)
                        }
                        is ScreenDestination.Playlist -> {
                            PlaylistScreen(navController = navController, id = screenDestination.id)
                        }
                        ScreenDestination.Setting -> {
                            SettingScreen()
                        }
                    }
                }
            }
        })
    {
        PlayScreen(panelState, it)
    }
}