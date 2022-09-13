package studio.mandysa.music.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Contactless
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.olshevski.navigation.reimagined.*
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.PanelState
import studio.mandysa.music.ui.common.SlidingPanel
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.screen.album.AlbumScreen
import studio.mandysa.music.ui.screen.controller.ControllerScreen
import studio.mandysa.music.ui.screen.fm.FmScreen
import studio.mandysa.music.ui.screen.home.HomeScreen
import studio.mandysa.music.ui.screen.me.MeMenu
import studio.mandysa.music.ui.screen.me.about.AboutScreen
import studio.mandysa.music.ui.screen.me.artistsub.ArtistSubScreen
import studio.mandysa.music.ui.screen.me.meplaylist.MePlaylistScreen
import studio.mandysa.music.ui.screen.me.meplaylist.playlistmenu.PlaylistMenu
import studio.mandysa.music.ui.screen.me.setting.SettingScreen
import studio.mandysa.music.ui.screen.message.Message
import studio.mandysa.music.ui.screen.play.PlayScreen
import studio.mandysa.music.ui.screen.playlist.PlaylistScreen
import studio.mandysa.music.ui.screen.search.SearchScreen
import studio.mandysa.music.ui.screen.singer.SingerScreen
import studio.mandysa.music.ui.screen.songmenu.SongMenu
import studio.mandysa.music.ui.screen.toplist.ToplistScreen
import studio.mandysa.music.ui.theme.anyBarColor
import studio.mandysa.music.ui.theme.isMedium

/**
 * Happy 22nd Birthday Shuangshengzi
 */
enum class HomeBottomNavigationDestination {
    Browse,
    Me,
}

val HomeBottomNavigationDestination.tabIcon
    get() = when (this) {
        HomeBottomNavigationDestination.Browse -> Icons.Rounded.Contactless
        HomeBottomNavigationDestination.Me -> Icons.Rounded.Person
    }

val HomeBottomNavigationDestination.tabName
    @Composable get() = when (this) {
        HomeBottomNavigationDestination.Browse -> stringResource(id = R.string.browse)
        HomeBottomNavigationDestination.Me -> stringResource(id = R.string.me)
    }

@Composable
fun AppNavigationDrawer(
    modifier: Modifier = Modifier,
    drawerContent: @Composable RowScope.() -> Unit,
    controllerBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    Row(modifier = modifier) {
        if (isMedium) {
            drawerContent.invoke(this)
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1.0f)
        ) {
            content.invoke(PaddingValues(bottom = with(LocalDensity.current) { size.height.toDp() }))
            Column(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .onSizeChanged {
                    size = it
                }) {
                controllerBar.invoke()
                if (!isMedium)
                    bottomBar.invoke()
            }

        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {

    val mainNavController =
        rememberNavController<ScreenDestination>(startDestination = ScreenDestination.Main)

    val homeNavController =
        rememberNavController(startDestination = HomeBottomNavigationDestination.Browse)

    val dialogNavController = rememberNavController<DialogDestination>(
        initialBackstack = emptyList()
    )

    var panelState by rememberSaveable {
        mutableStateOf<PanelState?>(null)
    }

    DialogNavHost(dialogNavController) { destination ->
        Dialog(
            onDismissRequest = { dialogNavController.pop() },
        ) {
            when (destination) {
                is DialogDestination.SongMenu -> {
                    SongMenu(mainNavController, dialogNavController, model = destination.model)
                }
                is DialogDestination.PlaylistMenu -> {
                    PlaylistMenu(mainNavController, dialogNavController, id = destination.id)
                }
                is DialogDestination.Message -> {
                    Message(dialogNavController, message = destination.message)
                }
                is DialogDestination.MeMenu -> {
                    MeMenu(mainNavController, dialogNavController)
                }
            }
        }
    }

    val systemUiController = rememberSystemUiController()

    systemUiController.setSystemBarsColor(
        Color.Transparent,
        if (panelState == PanelState.EXPANDED) false else !isSystemInDarkTheme(),
        isNavigationBarContrastEnforced = false
    )

    SlidingPanel(
        modifier = Modifier.fillMaxSize(),
        panelHeight = 0.dp,
        panelStateChange = {
            panelState = it
        },
        content = {
            AppNavigationDrawer(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                drawerContent = {
                    /*NavigationRail(
                        modifier = Modifier
                            .fillMaxHeight(),
                        containerColor = background
                    ) {
                        //上部分
                        val navLastDestination =
                            mainNavController.backstack.entries.last().destination
                        val bottomLastDestination =
                            homeNavController.backstack.entries.last().destination
                        HomeBottomNavigationDestination.values().forEach { screen ->
                            AppNavigationRailItem(
                                label = {
                                    Text(text = screen.tabName)
                                },
                                icon = {
                                    Icon(
                                        screen.tabIcon,
                                        contentDescription = null
                                    )
                                },
                                alwaysShowLabel = true,
                                selected = screen == bottomLastDestination,
                                onClick = {
                                    mainNavController.popUpTo {
                                        it == ScreenDestination.Main
                                    }
                                    if (!homeNavController.moveToTop { it == screen }) {
                                        homeNavController.navigate(screen)
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.weight(1.0f))
                        //下部分
                        // TODO: 待优化
                        val bottomRailItemFunction: @Composable (String, ImageVector, ScreenDestination) -> Unit =
                            { text, icon, screen ->
                                AppNavigationRailItem(
                                    label = {
                                        Text(text = text)
                                    },
                                    icon = {
                                        Icon(
                                            icon,
                                            contentDescription = null
                                        )
                                    },
                                    selected = navLastDestination == screen,
                                    onClick = {
                                        if (!mainNavController.moveToTop { it == screen }) {
                                            mainNavController.navigate(screen)
                                        }
                                    }
                                )
                            }
                        bottomRailItemFunction.invoke(
                            stringResource(id = R.string.search),
                            Icons.Rounded.Search,
                            ScreenDestination.Search
                        )
                    }*/
                },
                controllerBar = {
                    val isVisible = PlayManager.selectMusic != null
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        ControllerScreen(panelState) {
                            it.invoke(PanelState.EXPANDED)
                        }
                    }
                },
                bottomBar = {
                    AnimatedVisibility(
                        visible = mainNavController.backstack.entries.size <= 1,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        BottomNavigation(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding(),
                            backgroundColor = anyBarColor
                        ) {
                            val bottomLastDestination =
                                homeNavController.backstack.entries.last().destination
                            HomeBottomNavigationDestination.values().forEach { screen ->
                                BottomNavigationItem(
                                    selected = screen == bottomLastDestination,
                                    onClick = {
                                        if (!homeNavController.moveToTop { it == screen }) {
                                            homeNavController.navigate(screen)
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            screen.tabIcon,
                                            contentDescription = null
                                        )
                                    }, label = {
                                        Text(text = screen.tabName)
                                    })
                            }
                        }
                        /*NavigationBar(
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = anyBarColor,
                            contentColor = Color.White
                        ) {
                            val bottomLastDestination =
                                homeNavController.backstack.entries.last().destination
                            HomeBottomNavigationDestination.values().forEach { screen ->
                                AppNavigationBarItem(
                                    icon = {
                                        Icon(
                                            screen.tabIcon,
                                            contentDescription = null
                                        )
                                    }, label = {
                                        Text(text = screen.tabName)
                                    },
                                    selected = screen == bottomLastDestination,
                                    onClick = {
                                        if (!homeNavController.moveToTop { it == screen }) {
                                            homeNavController.navigate(screen)
                                        }
                                    }
                                )
                            }
                        }*/
                    }
                }
            ) { padding ->
                if (panelState != PanelState.EXPANDED) {
                    NavBackHandler(mainNavController)
                    //面板展开时返回事件不再分发到这里
                }
                //避免导航时面板处于展开状态
                LaunchedEffect(key1 = mainNavController.backstack) {
                    if (panelState == PanelState.EXPANDED) {
                        it.invoke(PanelState.COLLAPSED)
                    }
                }
                NavHost(mainNavController) { screenDestination ->
                    when (screenDestination) {
                        ScreenDestination.Main -> {
                            HomeScreen(
                                bottomNavController = homeNavController,
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding
                            )
                        }
                        ScreenDestination.Search -> {
                            SearchScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding
                            )
                        }
                        is ScreenDestination.Playlist -> {
                            PlaylistScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding,
                                id = screenDestination.id
                            )
                        }
                        is ScreenDestination.Singer -> {
                            SingerScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding,
                                id = screenDestination.id
                            )
                        }
                        is ScreenDestination.Album -> {
                            AlbumScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding,
                                id = screenDestination.id
                            )
                        }
                        ScreenDestination.About -> {
                            AboutScreen()
                        }
                        ScreenDestination.Setting -> {
                            SettingScreen()
                        }
                        ScreenDestination.ArtistSub -> {
                            ArtistSubScreen(
                                mainNavController = mainNavController,
                                paddingValues = padding
                            )
                        }
                        ScreenDestination.MePlaylist -> {
                            MePlaylistScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding
                            )
                        }
                        ScreenDestination.FmScreen -> {
                            FmScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding
                            )
                        }
                        ScreenDestination.ToplistScreen -> {
                            ToplistScreen()
                        }
                    }
                }
            }
        })
    {
        PlayScreen(
            mainNavController = mainNavController,
            dialogNavController = dialogNavController,
            panelState = panelState,
            it
        )
    }
}