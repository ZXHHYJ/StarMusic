package studio.mandysa.music.ui.screen.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.lifecycle.map
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.olshevski.navigation.reimagined.*
import dev.olshevski.navigation.reimagined.material.BottomSheetNavHost
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.*
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.screen.about.AboutScreen
import studio.mandysa.music.ui.screen.album.AlbumScreen
import studio.mandysa.music.ui.screen.albumcnt.AlbumCntScreen
import studio.mandysa.music.ui.screen.play.PlayScreen
import studio.mandysa.music.ui.screen.playlist.PlayListScreen
import studio.mandysa.music.ui.screen.scanmusic.ScanMusicScreen
import studio.mandysa.music.ui.screen.search.SearchScreen
import studio.mandysa.music.ui.screen.setting.SettingScreen
import studio.mandysa.music.ui.screen.singer.SingerScreen
import studio.mandysa.music.ui.screen.singercnt.SingerCntScreen
import studio.mandysa.music.ui.screen.single.SingleScreen
import studio.mandysa.music.ui.screen.theme.ThemeScreen
import studio.mandysa.music.ui.sheet.SongMenuSheet
import studio.mandysa.music.ui.sheet.songinfo.SongInfoSheet
import studio.mandysa.music.ui.theme.appBackgroundColor
import studio.mandysa.music.ui.theme.barBackgroundColor
import studio.mandysa.music.ui.theme.round

/**
 * Happy 22nd Birthday Shuangshengzi
 */


enum class HomeNavigationDestination {
    Single,
    Album,
    Singer,
    PlayList,
    Search,
}

val HomeNavigationDestination.tabIcon
    get() = when (this) {
        HomeNavigationDestination.Single -> Icons.Rounded.Source
        HomeNavigationDestination.Album -> Icons.Rounded.Album
        HomeNavigationDestination.Singer -> Icons.Rounded.Mic
        HomeNavigationDestination.PlayList -> Icons.Rounded.List
        HomeNavigationDestination.Search -> Icons.Rounded.Search
    }

val HomeNavigationDestination.tabName
    @Composable get() = when (this) {
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
                navigationBar.invoke()
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

    val sheetNavController = rememberNavController<BottomSheetDestination>(
        initialBackstack = emptyList()
    )

    var panelState by rememberSaveable {
        mutableStateOf<PanelState?>(null)
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
            ) { padding ->
                if (panelState != PanelState.EXPANDED) {
                    NavBackHandler(controller = mainNavController)
                    //面板展开时返回事件不再分发到这里
                }
                //避免导航时面板处于展开状态
                LaunchedEffect(mainNavController.backstack) {
                    if (panelState == PanelState.EXPANDED) {
                        it.invoke(PanelState.COLLAPSED)
                    }
                }
                Box(modifier = Modifier.statusBarsPadding()) {
                    val customTransitionSpec = NavTransitionSpec<Any?> { action, _, _ ->
                        val direction = if (action == NavAction.Pop) {
                            AnimatedContentScope.SlideDirection.End
                        } else {
                            AnimatedContentScope.SlideDirection.Start
                        }
                        slideIntoContainer(direction) with slideOutOfContainer(direction)
                    }
                    AnimatedNavHost(
                        controller = mainNavController,
                        transitionSpec = customTransitionSpec
                    ) { destination ->
                        when (destination) {
                            ScreenDestination.Main -> {
                                NavHost(homeNavController) {
                                    when (it) {
                                        HomeNavigationDestination.Single -> {
                                            SingleScreen(
                                                mainNavController = mainNavController,
                                                sheetNavController = sheetNavController,
                                                padding = padding
                                            )
                                        }
                                        HomeNavigationDestination.Album -> {
                                            AlbumScreen(
                                                mainNavController = mainNavController,
                                                sheetNavController = sheetNavController,
                                                padding = padding
                                            )
                                        }
                                        HomeNavigationDestination.Singer -> {
                                            SingerScreen(
                                                mainNavController = mainNavController,
                                                sheetNavController = sheetNavController,
                                                padding = padding
                                            )
                                        }
                                        HomeNavigationDestination.PlayList -> {
                                            PlayListScreen(
                                                mainNavController = mainNavController,
                                                sheetNavController = sheetNavController,
                                                padding = padding
                                            )
                                        }
                                        HomeNavigationDestination.Search -> {
                                            SearchScreen(
                                                mainNavController = mainNavController,
                                                sheetNavController = sheetNavController,
                                                padding = padding
                                            )
                                        }
                                    }
                                }
                            }

                            ScreenDestination.Search -> {
                                SearchScreen(
                                    mainNavController = mainNavController,
                                    sheetNavController = sheetNavController,
                                    padding = padding
                                )
                            }


                            is ScreenDestination.SingerCnt -> {
                                SingerCntScreen(
                                    mainNavController = mainNavController,
                                    sheetNavController = sheetNavController,
                                    padding = padding,
                                    artist = destination.artist
                                )
                            }

                            is ScreenDestination.AlbumCnt -> {
                                AlbumCntScreen(
                                    mainNavController = mainNavController,
                                    sheetNavController = sheetNavController,
                                    padding = padding,
                                    album = destination.album
                                )
                            }

                            ScreenDestination.About -> {
                                AboutScreen(
                                    mainNavController = mainNavController,
                                    sheetNavController = sheetNavController,
                                    padding = padding
                                )
                            }

                            ScreenDestination.Setting -> {
                                SettingScreen(
                                    mainNavController = mainNavController,
                                    sheetNavController = sheetNavController,
                                    padding = padding
                                )
                            }
                            ScreenDestination.Album -> {
                                AlbumScreen(
                                    mainNavController = mainNavController,
                                    sheetNavController = sheetNavController,
                                    padding = padding
                                )
                            }
                            ScreenDestination.ScanMusic -> {
                                ScanMusicScreen(
                                    mainNavController = mainNavController,
                                    sheetNavController = sheetNavController,
                                    padding = padding
                                )
                            }
                            ScreenDestination.Singer -> {
                                SingerScreen(
                                    mainNavController = mainNavController,
                                    sheetNavController = sheetNavController,
                                    padding = padding
                                )
                            }
                            ScreenDestination.PlayList -> {
                                PlayListScreen(
                                    mainNavController = mainNavController,
                                    sheetNavController = sheetNavController,
                                    padding = padding
                                )
                            }
                            ScreenDestination.Theme -> {
                                ThemeScreen(padding = padding)
                            }
                        }
                    }
                }
            }
        }) {
        PlayScreen(
            mainNavController = mainNavController,
            sheetNavController = sheetNavController,
            panelState = panelState,
            it
        )
    }

    BottomSheetNavHost(
        controller = sheetNavController,
        onDismissRequest = { sheetNavController.pop() }
    ) { destination ->
        BackHandler {
            sheetNavController.pop()
        }
        Column(
            modifier = Modifier.background(
                appBackgroundColor,
                shape = RoundedCornerShape(topStart = round, topEnd = round)
            )
        ) {
            when (destination) {
                is BottomSheetDestination.SongMenu -> {
                    SongMenuSheet(
                        mainNavController = mainNavController,
                        sheetNavController = sheetNavController,
                        song = destination.song
                    )
                }

                is BottomSheetDestination.SongInfo -> {
                    SongInfoSheet(
                        song = destination.song
                    )
                }

                is BottomSheetDestination.Message -> {
                    Message(sheetNavController = sheetNavController, message = destination.message)
                }

                is BottomSheetDestination.BottomSheet -> {
                    destination.content()
                }
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }

    }
}