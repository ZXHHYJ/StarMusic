package studio.mandysa.music.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Contactless
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Source
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.map
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.olshevski.navigation.reimagined.*
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.SettingRepository
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppNavigationBarItem
import studio.mandysa.music.ui.common.AppNavigationRailItem
import studio.mandysa.music.ui.common.PanelState
import studio.mandysa.music.ui.common.SlidingPanel
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.screen.album.AlbumScreen
import studio.mandysa.music.ui.screen.controller.ControllerScreen
import studio.mandysa.music.ui.screen.fm.FmScreen
import studio.mandysa.music.ui.screen.home.HomeScreen
import studio.mandysa.music.ui.screen.me.MeMenu
import studio.mandysa.music.ui.screen.me.MeScreen
import studio.mandysa.music.ui.screen.me.about.AboutScreen
import studio.mandysa.music.ui.screen.me.artistsub.ArtistSubScreen
import studio.mandysa.music.ui.screen.me.meplaylist.MePlaylistScreen
import studio.mandysa.music.ui.screen.me.meplaylist.playlistmenu.PlaylistMenu
import studio.mandysa.music.ui.screen.message.Message
import studio.mandysa.music.ui.screen.play.PlayScreen
import studio.mandysa.music.ui.screen.playlist.PlaylistScreen
import studio.mandysa.music.ui.screen.search.SearchScreen
import studio.mandysa.music.ui.screen.setting.SettingScreen
import studio.mandysa.music.ui.screen.singer.SingerScreen
import studio.mandysa.music.ui.screen.songmenu.SongMenu
import studio.mandysa.music.ui.screen.toplist.ToplistScreen
import studio.mandysa.music.ui.theme.*

/**
 * Happy 22nd Birthday Shuangshengzi
 */
enum class HomeBottomNavigationDestination {
    NeteaseCloud,
    Local,
}

val HomeBottomNavigationDestination.tabIcon
    get() = when (this) {
        HomeBottomNavigationDestination.NeteaseCloud -> Icons.Rounded.Contactless
        HomeBottomNavigationDestination.Local -> Icons.Rounded.Source
    }

val HomeBottomNavigationDestination.tabName
    @Composable get() = when (this) {
        HomeBottomNavigationDestination.NeteaseCloud -> stringResource(id = R.string.browse)
        HomeBottomNavigationDestination.Local -> stringResource(id = R.string.source)
    }

@Composable
fun AppNavigationDrawer(
    modifier: Modifier = Modifier,
    drawerContent: @Composable RowScope.() -> Unit,
    controllerBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val enableNeteaseCloud by SettingRepository.enableNeteaseCloud.observeAsState()
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    Row(modifier = modifier) {
        if (padMode && enableNeteaseCloud == true) {
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
                if (!padMode && enableNeteaseCloud == true) {
                    bottomBar.invoke()
                }
                Box(
                    modifier = Modifier
                        .height(LocalDensity.current.run {
                            WindowInsets.navigationBars
                                .getBottom(this)
                                .toDp()
                        })
                        .fillMaxWidth()
                        .background(barColor)
                )
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
        rememberNavController(startDestination = HomeBottomNavigationDestination.NeteaseCloud)

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
                modifier = Modifier.fillMaxSize(),
                drawerContent = {
                    NavigationRail(
                        modifier = Modifier
                            .fillMaxHeight()
                            .statusBarsPadding(),
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
                        NavigationBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(barColor),
                            containerColor = Color.Transparent,
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
                        }
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
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
                                SettingScreen(
                                    mainNavController = mainNavController,
                                    dialogNavController = dialogNavController,
                                    paddingValues = padding
                                )
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

                            ScreenDestination.MeScreen -> {
                                MeScreen(
                                    mainNavController,
                                    dialogNavController,
                                    padding
                                )
                            }
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