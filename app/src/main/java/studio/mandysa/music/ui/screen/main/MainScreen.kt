package studio.mandysa.music.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.map
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.olshevski.navigation.reimagined.*
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.SettingRepository
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.*
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.screen.about.AboutScreen
import studio.mandysa.music.ui.screen.local.album.AlbumScreen
import studio.mandysa.music.ui.screen.local.albumcnt.AlbumCntScreen
import studio.mandysa.music.ui.screen.local.playlist.PlayListScreen
import studio.mandysa.music.ui.screen.local.singer.SingerScreen
import studio.mandysa.music.ui.screen.local.singercnt.SingerCntScreen
import studio.mandysa.music.ui.screen.local.single.SingleScreen
import studio.mandysa.music.ui.screen.play.PlayScreen
import studio.mandysa.music.ui.screen.search.SearchScreen
import studio.mandysa.music.ui.screen.setting.SettingScreen
import studio.mandysa.music.ui.screen.songmenu.SongMenu
import studio.mandysa.music.ui.theme.appRightNavBarWidth
import studio.mandysa.music.ui.theme.barBackgroundColor

/**
 * Happy 22nd Birthday Shuangshengzi
 */
enum class HomeNavigationDestination {
    CloudMusic,
    Single,
    Album,
    Singer,
    PlayList,
    Search,
}

val HomeNavigationDestination.tabIcon
    get() = when (this) {
        HomeNavigationDestination.CloudMusic -> Icons.Rounded.Contactless
        HomeNavigationDestination.Single -> Icons.Rounded.Source
        HomeNavigationDestination.Album -> Icons.Rounded.Album
        HomeNavigationDestination.Singer -> Icons.Rounded.Mic
        HomeNavigationDestination.PlayList -> Icons.Rounded.List
        HomeNavigationDestination.Search -> Icons.Rounded.Search
    }

val HomeNavigationDestination.tabName
    @Composable get() = when (this) {
        HomeNavigationDestination.CloudMusic -> stringResource(id = R.string.cloud_music)
        HomeNavigationDestination.Single -> stringResource(id = R.string.media_lib)
        HomeNavigationDestination.Album -> stringResource(id = R.string.album)
        HomeNavigationDestination.Singer -> stringResource(id = R.string.singer)
        HomeNavigationDestination.PlayList -> stringResource(id = R.string.play_list)
        HomeNavigationDestination.Search -> stringResource(id = R.string.search)
    }

@Composable
private fun AppScaffold(
    modifier: Modifier = Modifier,
    controllerBar: @Composable () -> Unit,
    navigationBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Row(modifier = modifier) {
        if (SettingRepository.isTabletMode) {
            Box(modifier = Modifier.fillMaxHeight()) {
                navigationBar.invoke()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1.0f)
        ) {
            var bottomBarSize by remember {
                mutableStateOf(IntSize.Zero)
            }
            content.invoke(PaddingValues(bottom = with(LocalDensity.current) {
                bottomBarSize.height.toDp()
            }))
            Column(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .onSizeChanged {
                    bottomBarSize = it
                }) {
                controllerBar.invoke()
                if (!SettingRepository.isTabletMode) {
                    navigationBar.invoke()
                }
                Box(
                    modifier = Modifier
                        .height(
                            with(LocalDensity.current) {
                                WindowInsets.navigationBars
                                    .getBottom(this)
                                    .toDp()
                            })
                        .fillMaxWidth()
                        .background(barBackgroundColor)
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
        rememberNavController(startDestination = HomeNavigationDestination.Single)

    val dialogNavController = rememberNavController<DialogDestination>(
        initialBackstack = emptyList()
    )

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

                }

                is DialogDestination.Message -> {
                    Message(dialogNavController, message = destination.message)
                }

                is DialogDestination.MeMenu -> {

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
            val mediaControllerVisibility by PlayManager.changeMusicLiveData().map {
                return@map true
            }.observeAsState(false)
            AppScaffold(
                modifier = Modifier.fillMaxSize(),
                controllerBar = {
                    AnimatedVisibility(
                        visible = mediaControllerVisibility,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        MediaController(
                            modifier = Modifier.fillMaxWidth(),
                            panelState = panelState
                        ) {
                            it.invoke(PanelState.EXPANDED)
                        }
                    }
                },
                navigationBar = {
                    if (SettingRepository.isTabletMode) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(appRightNavBarWidth)
                        ) {
                            TopAppBar(
                                state = rememberTopAppBarState(),
                                modifier = Modifier.fillMaxWidth(),
                                title = stringResource(
                                    id = R.string.music
                                )
                            ) {

                            }
                        }
                    } else {
                        AnimatedVisibility(
                            visible = mainNavController.backstack.entries.size <= 1,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            AppBottomNavigationBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                            ) {
                                val bottomLastDestination =
                                    homeNavController.backstack.entries.last().destination
                                listOf(
                                    HomeNavigationDestination.Single,
                                    HomeNavigationDestination.Search
                                ).forEach { item ->
                                    BottomNavigationItem(
                                        icon = {
                                            Icon(
                                                item.tabIcon,
                                                contentDescription = null
                                            )
                                        }, label = {
                                            Text(text = item.tabName)
                                        },
                                        selected = item == bottomLastDestination,
                                        onClick = {
                                            if (!homeNavController.moveToTop { it == item }) {
                                                homeNavController.navigate(item)
                                            }
                                        }
                                    )
                                }
                            }
                            if (!mediaControllerVisibility) {
                                AppDivider(modifier = Modifier.fillMaxWidth())
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

                                    }
                                    HomeNavigationDestination.Single -> {
                                        SingleScreen(
                                            mainNavController,
                                            dialogNavController,
                                            padding
                                        )
                                    }
                                    HomeNavigationDestination.Album -> {
                                        AlbumScreen(
                                            mainNavController,
                                            dialogNavController,
                                            padding
                                        )
                                    }
                                    HomeNavigationDestination.Singer -> {
                                        SingerScreen(
                                            mainNavController,
                                            dialogNavController,
                                            padding
                                        )
                                    }
                                    HomeNavigationDestination.PlayList -> {
                                        PlayListScreen(
                                            mainNavController,
                                            dialogNavController,
                                            padding
                                        )
                                    }
                                    HomeNavigationDestination.Search -> {
                                        SearchScreen(
                                            mainNavController = mainNavController,
                                            dialogNavController = dialogNavController,
                                            paddingValues = padding
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

                        }

                        is ScreenDestination.AlbumCnt -> {
                            AlbumCntScreen(
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

                        }

                        ScreenDestination.CloudMePlaylist -> {

                        }

                        ScreenDestination.CloudFmScreen -> {

                        }

                        ScreenDestination.CloudToplistScreen -> {

                        }

                        ScreenDestination.CloudMeScreen -> {

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