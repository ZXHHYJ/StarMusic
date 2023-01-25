package studio.mandysa.music.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
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
import studio.mandysa.music.logic.repository.UserRepository
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.*
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.screen.about.AboutScreen
import studio.mandysa.music.ui.screen.cloud.fm.CloudFmScreen
import studio.mandysa.music.ui.screen.cloud.me.CloudMeMenu
import studio.mandysa.music.ui.screen.cloud.me.CloudMeScreen
import studio.mandysa.music.ui.screen.cloud.me.artistsub.CloudArtistSubScreen
import studio.mandysa.music.ui.screen.cloud.me.meplaylist.CloudMePlaylistScreen
import studio.mandysa.music.ui.screen.cloud.me.meplaylist.playlistmenu.CloudPlaylistMenu
import studio.mandysa.music.ui.screen.cloud.music.CloudMusicScreen
import studio.mandysa.music.ui.screen.cloud.playlistcnt.CloudPlaylistCntScreen
import studio.mandysa.music.ui.screen.cloud.singercnt.CloudSingerCntScreen
import studio.mandysa.music.ui.screen.local.album.AlbumScreen
import studio.mandysa.music.ui.screen.local.playlist.PlayListScreen
import studio.mandysa.music.ui.screen.local.singer.SingerScreen
import studio.mandysa.music.ui.screen.local.singercnt.SingerCntScreen
import studio.mandysa.music.ui.screen.local.single.SingleScreen
import studio.mandysa.music.ui.screen.play.PlayScreen
import studio.mandysa.music.ui.screen.search.SearchScreen
import studio.mandysa.music.ui.screen.setting.SettingScreen
import studio.mandysa.music.ui.screen.songmenu.SongMenu
import studio.mandysa.music.ui.theme.*

/**
 * Happy 22nd Birthday Shuangshengzi
 */
enum class HomeNavigationDestination {
    CloudMusic,
    Single,
    Album,
    Singer,
    PlayList,
}

val HomeNavigationDestination.tabIcon
    get() = when (this) {
        HomeNavigationDestination.CloudMusic -> Icons.Rounded.Contactless
        HomeNavigationDestination.Single -> Icons.Rounded.Source
        HomeNavigationDestination.Album -> Icons.Rounded.Album
        HomeNavigationDestination.Singer -> Icons.Rounded.Mic
        HomeNavigationDestination.PlayList -> Icons.Rounded.List
    }

val HomeNavigationDestination.tabName
    @Composable get() = when (this) {
        HomeNavigationDestination.CloudMusic -> stringResource(id = R.string.cloud_music)
        HomeNavigationDestination.Single -> stringResource(id = R.string.source)
        HomeNavigationDestination.Album -> stringResource(id = R.string.album)
        HomeNavigationDestination.Singer -> stringResource(id = R.string.singer)
        HomeNavigationDestination.PlayList -> stringResource(id = R.string.play_list)
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppNavigationDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    drawerContent: @Composable () -> Unit,
    controllerBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val contentBox: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxSize()) {
            content.invoke(PaddingValues(bottom = with(LocalDensity.current) {
                size.height.toDp()
            }))
            Column(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .onSizeChanged {
                    size = it
                }) {
                controllerBar.invoke()
                if (!isAndroidPad) {
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
    when (isAndroidPad) {
        true -> {
            PermanentNavigationDrawer(
                modifier = modifier,
                drawerContent = drawerContent,
                content = contentBox
            )
        }
        false -> {
            ModalNavigationDrawer(
                modifier = modifier,
                drawerState = drawerState,
                drawerContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 50.dp)
                    ) {
                        drawerContent.invoke()
                    }
                },
                content = contentBox
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val mainNavController =
        rememberNavController<ScreenDestination>(startDestination = ScreenDestination.Main)

    val homeNavController =
        rememberNavController(startDestination = HomeNavigationDestination.Single)

    val dialogNavController = rememberNavController<DialogDestination>(
        initialBackstack = emptyList()
    )

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    var panelState by rememberSaveable {
        mutableStateOf<PanelState?>(null)
    }

    DialogNavHost(dialogNavController) { destination ->
        Dialog(
            onDismissRequest = {
                dialogNavController.pop()
            },
        ) {
            when (destination) {
                is DialogDestination.SongMenu -> {
                    SongMenu(mainNavController, dialogNavController, song = destination.song)
                }

                is DialogDestination.PlaylistMenu -> {
                    CloudPlaylistMenu(mainNavController, dialogNavController, id = destination.id)
                }

                is DialogDestination.Message -> {
                    Message(dialogNavController, message = destination.message)
                }

                is DialogDestination.MeMenu -> {
                    CloudMeMenu(mainNavController, dialogNavController)
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
                drawerState = drawerState,
                drawerContent = {
                    NavigationRail(
                        modifier = Modifier
                            .fillMaxHeight(),
                        containerColor = background
                    ) {
                        val navLastDestination =
                            mainNavController.backstack.entries.last().destination
                        val bottomLastDestination =
                            homeNavController.backstack.entries.last().destination
                        val isLoginState by UserRepository.isLoginLiveData.observeAsState()
                        if (!isAndroidPad) {
                            if (isLoginState == true) {
                                HomeNavigationDestination.CloudMusic.let { screen ->
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
                                        selected = screen == bottomLastDestination,
                                        onClick = {
                                            mainNavController.popUpTo {
                                                it == ScreenDestination.Main
                                            }
                                            if (!homeNavController.moveToTop {
                                                    it == screen
                                                }) {
                                                homeNavController.navigate(screen)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        HomeNavigationDestination.values().forEach { screen ->
                            if (screen == HomeNavigationDestination.CloudMusic) {
                                return@forEach
                            }
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
                                selected = screen == bottomLastDestination,
                                onClick = {
                                    mainNavController.popUpTo {
                                        it == ScreenDestination.Main
                                    }
                                    if (!homeNavController.moveToTop {
                                            it == screen
                                        }) {
                                        homeNavController.navigate(screen)
                                    }
                                }
                            )
                        }

                        @Composable
                        fun BottomNavRailItem(
                            text: String,
                            icon: ImageVector,
                            screen: ScreenDestination
                        ) {
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
                                    if (!mainNavController.moveToTop {
                                            it == screen
                                        }) {
                                        mainNavController.navigate(screen)
                                    }
                                }
                            )
                        }

                        if (isAndroidPad) {
                            Spacer(modifier = Modifier.weight(1.0f))
                            BottomNavRailItem(
                                stringResource(id = R.string.search),
                                Icons.Rounded.Search,
                                ScreenDestination.Search
                            )
                        }
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
                        MediaController(panelState) {
                            it.invoke(PanelState.EXPANDED)
                        }
                    }
                },
                bottomBar = {
                    val isLoginState by UserRepository.isLoginLiveData.observeAsState()
                    AnimatedVisibility(
                        visible = mainNavController.backstack.entries.size <= 1 && isLoginState == true,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        NavigationBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(barColor),
                            containerColor = Color.Transparent,
                            contentColor = Color.White,
                            windowInsets = WindowInsets(0, 0, 0, 0)
                        ) {
                            val bottomLastDestination =
                                homeNavController.backstack.entries.last().destination
                            AppNavigationBarItem(
                                icon = {
                                    Icon(
                                        HomeNavigationDestination.CloudMusic.tabIcon,
                                        contentDescription = null
                                    )
                                }, label = {
                                    Text(text = HomeNavigationDestination.CloudMusic.tabName)
                                },
                                selected = HomeNavigationDestination.CloudMusic == bottomLastDestination,
                                onClick = {
                                    if (!homeNavController.moveToTop {
                                            it == HomeNavigationDestination.CloudMusic
                                        }) {
                                        homeNavController.navigate(
                                            HomeNavigationDestination.CloudMusic
                                        )
                                    }
                                }
                            )
                            AppNavigationBarItem(
                                icon = {
                                    Icon(
                                        HomeNavigationDestination.Single.tabIcon,
                                        contentDescription = null
                                    )
                                }, label = {
                                    Text(text = HomeNavigationDestination.Single.tabName)
                                },
                                selected = HomeNavigationDestination.CloudMusic != bottomLastDestination,
                                onClick = {
                                    if (!homeNavController.moveToTop {
                                            it == HomeNavigationDestination.Single
                                        }) {
                                        homeNavController.navigate(
                                            HomeNavigationDestination.Single
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            ) { padding ->
                if (panelState != PanelState.EXPANDED) {
                    NavBackHandler(mainNavController)
                    //面板展开时返回事件不再分发到这里
                }
                //避免导航时面板处于展开状态
                LaunchedEffect(mainNavController.backstack) {
                    if (panelState == PanelState.EXPANDED) {
                        it.invoke(PanelState.COLLAPSED)
                    }
                }
                NavHost(mainNavController) { screenDestination ->
                    when (screenDestination) {
                        ScreenDestination.Main -> {
                            NavHost(homeNavController) {
                                when (it) {
                                    HomeNavigationDestination.CloudMusic -> {
                                        CloudMusicScreen(
                                            mainNavController,
                                            dialogNavController,
                                            padding
                                        )
                                    }
                                    HomeNavigationDestination.Single -> {
                                        SingleScreen(
                                            mainNavController,
                                            dialogNavController,
                                            drawerState,
                                            padding
                                        )
                                    }
                                    HomeNavigationDestination.Album -> {
                                        AlbumScreen(
                                            mainNavController,
                                            dialogNavController,
                                            drawerState,
                                            padding
                                        )
                                    }
                                    HomeNavigationDestination.Singer -> {
                                        SingerScreen(
                                            mainNavController,
                                            dialogNavController,
                                            drawerState,
                                            padding
                                        )
                                    }
                                    HomeNavigationDestination.PlayList -> {
                                        PlayListScreen(
                                            mainNavController,
                                            dialogNavController,
                                            drawerState,
                                            padding
                                        )
                                    }
                                }
                            }
                        }

                        ScreenDestination.Search -> {
                            SearchScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding
                            )
                        }

                        is ScreenDestination.CloudPlaylistCnt -> {
                            CloudPlaylistCntScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding,
                                id = screenDestination.id
                            )
                        }

                        is ScreenDestination.SingerCnt -> {
                            SingerCntScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding,
                                artist = screenDestination.artist
                            )
                        }

                        is ScreenDestination.CloudSingerCnt -> {
                            CloudSingerCntScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding,
                                id = screenDestination.artist.id
                            )
                            // TODO: 需要优化
                        }

                        is ScreenDestination.AlbumCnt -> {
                            studio.mandysa.music.ui.screen.local.albumcnt.AlbumCntScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding,
                                album = screenDestination.album
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

                        ScreenDestination.CloudArtistSub -> {
                            CloudArtistSubScreen(
                                mainNavController = mainNavController,
                                paddingValues = padding
                            )
                        }

                        ScreenDestination.CloudMePlaylist -> {
                            CloudMePlaylistScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding
                            )
                        }

                        ScreenDestination.CloudFmScreen -> {
                            CloudFmScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = padding
                            )
                        }

                        ScreenDestination.CloudToplistScreen -> {
                            // TODO:
                        }

                        ScreenDestination.CloudMeScreen -> {
                            CloudMeScreen(
                                mainNavController,
                                dialogNavController,
                                padding
                            )
                        }
                    }
                }
            }
        }) {
        PlayScreen(
            mainNavController = mainNavController,
            dialogNavController = dialogNavController,
            panelState = panelState,
            it
        )
    }
}