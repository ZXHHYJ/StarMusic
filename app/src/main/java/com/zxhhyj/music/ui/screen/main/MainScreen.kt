package com.zxhhyj.music.ui.screen.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Source
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.map
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.coverUrl
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.common.AlbumMotionBlur
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.common.PanelState
import com.zxhhyj.music.ui.common.SlidingPanel
import com.zxhhyj.music.ui.common.rememberPanelController
import com.zxhhyj.music.ui.dialog.CreatePlayListDialog
import com.zxhhyj.music.ui.dialog.EditPlayListTitleDialog
import com.zxhhyj.music.ui.dialog.MediaLibsEmptyDialog
import com.zxhhyj.music.ui.dialog.ScanMusicDialog
import com.zxhhyj.music.ui.dialog.SplashDialog
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.screen.about.AboutScreen
import com.zxhhyj.music.ui.screen.album.AlbumScreen
import com.zxhhyj.music.ui.screen.albumcnt.AlbumCntScreen
import com.zxhhyj.music.ui.screen.folder.FolderScreen
import com.zxhhyj.music.ui.screen.hidesong.HiddenSongScreen
import com.zxhhyj.music.ui.screen.lab.LabScreen
import com.zxhhyj.music.ui.screen.lyric.LyricScreen
import com.zxhhyj.music.ui.screen.medialibs.MediaSourceScreen
import com.zxhhyj.music.ui.screen.misc.MiscScreen
import com.zxhhyj.music.ui.screen.more.MoreScreen
import com.zxhhyj.music.ui.screen.personalize.PersonalizeScreen
import com.zxhhyj.music.ui.screen.play.PlayScreen
import com.zxhhyj.music.ui.screen.playlist.PlayListScreen
import com.zxhhyj.music.ui.screen.playlistcnt.PlayListCntScreen
import com.zxhhyj.music.ui.screen.search.SearchScreen
import com.zxhhyj.music.ui.screen.singer.SingerScreen
import com.zxhhyj.music.ui.screen.singercnt.SingerCntScreen
import com.zxhhyj.music.ui.screen.single.SingleScreen
import com.zxhhyj.music.ui.screen.vip.VipScreen
import com.zxhhyj.music.ui.sheet.AddToPlayListSheet
import com.zxhhyj.music.ui.sheet.PlaylistMenuSheet
import com.zxhhyj.music.ui.sheet.SongMenuSheet
import com.zxhhyj.music.ui.sheet.SongSortSheet
import com.zxhhyj.music.ui.sheet.songinfo.SongInfoSheet
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.round
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCard
import com.zxhhyj.ui.view.AppDivider
import com.zxhhyj.ui.view.AppIconButton
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.DialogNavHost
import dev.olshevski.navigation.reimagined.NavAction
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.material.BottomSheetNavHost
import dev.olshevski.navigation.reimagined.material.BottomSheetProperties
import dev.olshevski.navigation.reimagined.moveToTop
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.popAll
import dev.olshevski.navigation.reimagined.rememberNavController

/**
 * Happy 22nd Birthday Shuangshengzi
 */

enum class HomeNavigationDestination {
    Single,
    More,
}

val HomeNavigationDestination.tabIcon
    get() = when (this) {
        HomeNavigationDestination.Single -> Icons.Rounded.Source
        HomeNavigationDestination.More -> Icons.Rounded.MoreHoriz
    }

val HomeNavigationDestination.tabName
    @Composable get() = when (this) {
        HomeNavigationDestination.Single -> stringResource(id = R.string.media_lib)
        HomeNavigationDestination.More -> stringResource(id = R.string.more)
    }

@Composable
private fun Scaffold(
    modifier: Modifier = Modifier,
    controllerBar: @Composable () -> Unit,
    navigationBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Box(
        modifier = modifier
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
                    .fillMaxWidth()
                    .background(LocalColorScheme.current.highBackground)
            ) {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
fun MainScreen() {

    val mainNavController =
        rememberNavController<ScreenDestination>(startDestination = ScreenDestination.Main)

    val homeNavController =
        rememberNavController(startDestination = HomeNavigationDestination.Single)

    val dialogNavController = rememberNavController<DialogDestination>(
        initialBackstack = emptyList()
    )

    val sheetNavController = rememberNavController<SheetDestination>(
        initialBackstack = emptyList()
    )

    val panelController = rememberPanelController(panelState = PanelState.COLLAPSED)

    val systemUiController = rememberSystemUiController()

    systemUiController.setSystemBarsColor(
        Color.Transparent,
        if (panelController.panelState == PanelState.EXPANDED) false else !isSystemInDarkTheme(),
        isNavigationBarContrastEnforced = false
    )

    SlidingPanel(
        modifier = Modifier.fillMaxSize(),
        panelHeight = 0.dp,
        panelController = panelController,
        content = {
            val visibilityMediaController by PlayManager.currentSongLiveData().map {
                return@map it != null
            }.observeAsState(false)
            if (!visibilityMediaController && panelController.panelState == PanelState.EXPANDED) {
                panelController.swipeTo(PanelState.COLLAPSED)
            }
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                controllerBar = {
                    AnimatedVisibility(
                        visible = visibilityMediaController,
                        enter = if (SettingRepository.EnableLinkUI) EnterTransition.None else expandVertically(),
                        exit = if (SettingRepository.EnableLinkUI) ExitTransition.None else shrinkVertically(),
                    ) {
                        val controlBarHeight = 50.dp
                        val coverLength = 56.dp
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .height(controlBarHeight / 2)
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(LocalColorScheme.current.highBackground)
                            ) {
                                AppDivider(modifier = Modifier.fillMaxWidth())
                            }
                            Box(modifier = Modifier.padding(horizontal = horizontal)) {
                                val song by PlayManager.currentSongLiveData().observeAsState()
                                AppCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(controlBarHeight)
                                        .align(Alignment.BottomCenter),
                                    backgroundColor = Color.Transparent,
                                    contentColor = Color.Transparent,
                                ) {
                                    when (SettingRepository.EnableLinkUI) {
                                        true -> {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .align(Alignment.BottomCenter)
                                                    .height(controlBarHeight)
                                                    .background(Color.DarkGray)
                                                    .clickable {
                                                        panelController.swipeTo(PanelState.EXPANDED)
                                                    }
                                            )
                                        }

                                        false -> {
                                            AlbumMotionBlur(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .align(Alignment.BottomCenter)
                                                    .height(controlBarHeight)
                                                    .background(Color.DarkGray)
                                                    .clickable {
                                                        panelController.swipeTo(PanelState.EXPANDED)
                                                    },
                                                albumUrl = song?.album?.coverUrl,
                                                paused = panelController.panelState != PanelState.COLLAPSED
                                            )
                                        }
                                    }
                                    Row(
                                        modifier = Modifier
                                            .padding(start = coverLength)
                                            .fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .padding(start = horizontal)
                                                .weight(1.0f),
                                            text = song?.songName ?: "",
                                            fontSize = 16.sp,
                                            maxLines = 1,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        val playPauseState by PlayManager.pauseLiveData().map {
                                            if (it) R.drawable.ic_play else R.drawable.ic_pause
                                        }.observeAsState(R.drawable.ic_play)
                                        val buttonSize = 36.dp
                                        AppIconButton(onClick = {
                                            if (PlayManager.pauseLiveData().value == true) {
                                                PlayManager.start()
                                            } else {
                                                PlayManager.pause()
                                            }
                                        }) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(
                                                    playPauseState
                                                ),
                                                tint = Color.White,
                                                contentDescription = null,
                                                modifier = Modifier.size(buttonSize)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(horizontal / 2))
                                        AppIconButton(onClick = { PlayManager.skipToNext() }) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.ic_skip_next),
                                                tint = Color.White,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(buttonSize)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(horizontal / 2))
                                    }
                                }
                                AppAsyncImage(
                                    modifier = Modifier.size(coverLength),
                                    data = song?.album?.coverUrl ?: ""
                                )
                            }
                        }
                    }
                },
                navigationBar = {
                    AnimatedVisibility(
                        visible = mainNavController.backstack.entries.size <= 1,
                        enter = if (SettingRepository.EnableLinkUI) EnterTransition.None else expandVertically(),
                        exit = if (SettingRepository.EnableLinkUI) ExitTransition.None else shrinkVertically()
                    ) {
                        BottomNavigation(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = LocalColorScheme.current.highBackground,
                            contentColor = LocalColorScheme.current.highlight,
                            elevation = 0.dp
                        ) {
                            val bottomLastDestination =
                                homeNavController.backstack.entries.last().destination
                            HomeNavigationDestination.values().forEach { item ->
                                BottomNavigationItem(
                                    icon = { Icon(item.tabIcon, contentDescription = item.name) },
                                    label = { Text(text = item.tabName) },
                                    selected = item == bottomLastDestination,
                                    unselectedContentColor = LocalColorScheme.current.disabled,
                                    onClick = {
                                        if (!homeNavController.moveToTop { it == item }) {
                                            homeNavController.navigate(item)
                                        }
                                    }
                                )
                            }
                        }
                        if (!visibilityMediaController) {
                            AppDivider(modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            ) { paddingValues ->
                if (panelController.panelState != PanelState.EXPANDED) {
                    NavBackHandler(controller = mainNavController)
                    //面板展开时返回事件不再分发到这里
                }
                //避免导航时面板处于展开状态
                LaunchedEffect(mainNavController.backstack) {
                    if (panelController.panelState == PanelState.EXPANDED) {
                        panelController.swipeTo(PanelState.COLLAPSED)
                    }
                }
                AnimatedNavHost(
                    controller = mainNavController,
                    transitionSpec = { action, _, _ ->
                        if (SettingRepository.EnableLinkUI) {
                            return@AnimatedNavHost EnterTransition.None togetherWith ExitTransition.None
                        }
                        val direction = if (action == NavAction.Pop) {
                            AnimatedContentTransitionScope.SlideDirection.End
                        } else {
                            AnimatedContentTransitionScope.SlideDirection.Start
                        }
                        slideIntoContainer(direction) togetherWith slideOutOfContainer(direction)
                    }
                ) { destination ->
                    when (destination) {
                        ScreenDestination.Main -> {
                            NavHost(controller = homeNavController) {
                                when (it) {
                                    HomeNavigationDestination.Single -> {
                                        SingleScreen(
                                            mainNavController = mainNavController,
                                            sheetNavController = sheetNavController,
                                            paddingValues = paddingValues
                                        )
                                    }

                                    HomeNavigationDestination.More -> {
                                        MoreScreen(
                                            mainNavController = mainNavController,
                                            paddingValues = paddingValues
                                        )
                                    }
                                }
                            }

                        }

                        is ScreenDestination.Search -> {
                            SearchScreen(
                                mainNavController = mainNavController,
                                sheetNavController = sheetNavController,
                                paddingValues = paddingValues,
                                start = destination.start
                            )
                        }

                        is ScreenDestination.SingerCnt -> {
                            SingerCntScreen(
                                sheetNavController = sheetNavController,
                                paddingValues = paddingValues,
                                artist = destination.artist
                            )
                        }

                        is ScreenDestination.AlbumCnt -> {
                            AlbumCntScreen(
                                mainNavController = mainNavController,
                                sheetNavController = sheetNavController,
                                paddingValues = paddingValues,
                                album = destination.album
                            )
                        }

                        ScreenDestination.About -> {
                            AboutScreen(
                                paddingValues = paddingValues
                            )
                        }

                        ScreenDestination.Setting -> {
                            MoreScreen(
                                mainNavController = mainNavController,
                                paddingValues = paddingValues
                            )
                        }

                        ScreenDestination.Album -> {
                            AlbumScreen(
                                mainNavController = mainNavController,
                                paddingValues = paddingValues
                            )
                        }

                        ScreenDestination.MediaLibs -> {
                            MediaSourceScreen(
                                mainNavController = mainNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = paddingValues
                            )
                        }

                        ScreenDestination.Singer -> {
                            SingerScreen(
                                mainNavController = mainNavController,
                                paddingValues = paddingValues
                            )
                        }

                        ScreenDestination.PlayList -> {
                            PlayListScreen(
                                mainNavController = mainNavController,
                                sheetNavController = sheetNavController,
                                dialogNavController = dialogNavController,
                                paddingValues = paddingValues
                            )
                        }

                        ScreenDestination.Lyric -> {
                            LyricScreen(paddingValues = paddingValues)
                        }

                        ScreenDestination.HiddenSong -> {
                            HiddenSongScreen(paddingValues = paddingValues)
                        }

                        is ScreenDestination.PlayListCnt -> {
                            PlayListCntScreen(
                                playlist = destination.model,
                                sheetNavController = sheetNavController,
                                paddingValues = paddingValues
                            )
                        }

                        ScreenDestination.Lab -> {
                            LabScreen(paddingValues = paddingValues)
                        }

                        ScreenDestination.Folder -> {
                            FolderScreen(paddingValues = paddingValues)
                        }

                        ScreenDestination.Vip -> {
                            VipScreen(paddingValues = paddingValues)
                        }

                        ScreenDestination.Misc -> {
                            MiscScreen(paddingValues = paddingValues)
                        }

                        ScreenDestination.Personalize -> {
                            PersonalizeScreen(paddingValues = paddingValues)
                        }
                    }
                }
            }
        }) {
        PlayScreen(
            sheetNavController = sheetNavController,
            panelController = panelController
        )
    }

    val onDismissRequest: () -> Unit = {
        dialogNavController.pop()
    }
    DialogNavHost(controller = dialogNavController) {
        when (it) {
            DialogDestination.MediaLibsEmpty -> {
                MediaLibsEmptyDialog(
                    onDismissRequest = onDismissRequest,
                    mainNavController = mainNavController
                )
            }

            DialogDestination.Splash -> {
                SplashDialog(
                    onDismissRequest = onDismissRequest,
                    dialogNavController = dialogNavController
                )
            }

            DialogDestination.CreatePlayList -> {
                CreatePlayListDialog(onDismissRequest = onDismissRequest)
            }

            DialogDestination.ScanMusic -> {
                ScanMusicDialog(onDismissRequest = onDismissRequest)
            }

            is DialogDestination.EditPlayListTitle -> {
                EditPlayListTitleDialog(onDismissRequest = onDismissRequest, model = it.model)
            }
        }
    }

    DisposableEffect(Unit) {
        if (!SettingRepository.AgreePrivacyPolicy) {
            dialogNavController.navigate(DialogDestination.Splash)
        }
        onDispose {
            dialogNavController.popAll()
        }
    }

    BottomSheetNavHost(
        controller = sheetNavController,
        onDismissRequest = { sheetNavController.pop() },
        sheetPropertiesSpec = {
            BottomSheetProperties(skipHalfExpanded = true)
        }
    ) { destination ->
        BackHandler {
            sheetNavController.pop()
        }
        Column(
            modifier = Modifier
                .background(
                    LocalColorScheme.current.background,
                    shape = RoundedCornerShape(topStart = round, topEnd = round)
                )
        ) {
            Spacer(
                modifier = Modifier
                    .padding(vertical = vertical)
                    .width(50.dp)
                    .height(6.dp)
                    .background(
                        LocalColorScheme.current.subText.copy(0.3f),
                        RoundedCornerShape(50)
                    )
                    .align(Alignment.CenterHorizontally)
            )
            when (destination) {
                is SheetDestination.SongMenu -> {
                    SongMenuSheet(
                        mainNavController = mainNavController,
                        sheetNavController = sheetNavController,
                        song = destination.song
                    )
                }

                is SheetDestination.SongInfo -> {
                    SongInfoSheet(
                        song = destination.song
                    )
                }

                is SheetDestination.AddToPlayList -> {
                    AddToPlayListSheet(
                        dialogNavController = dialogNavController,
                        sheetNavController = sheetNavController,
                        song = destination.song
                    )
                }

                is SheetDestination.PlaylistMenu -> {
                    PlaylistMenuSheet(
                        mainNavController = mainNavController,
                        dialogNavController = dialogNavController,
                        sheetNavController = sheetNavController,
                        model = destination.model
                    )
                }

                SheetDestination.SongSort -> {
                    SongSortSheet()
                }
            }
            Box(modifier = Modifier.heightIn(min = vertical)) {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }

    }
}