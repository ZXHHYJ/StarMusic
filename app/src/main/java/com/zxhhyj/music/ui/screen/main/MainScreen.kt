package com.zxhhyj.music.ui.screen.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.map
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.common.AppDivider
import com.zxhhyj.music.ui.common.MediaController
import com.zxhhyj.music.ui.common.PanelState
import com.zxhhyj.music.ui.common.SlidingPanel
import com.zxhhyj.music.ui.dialog.CreatePlayListDialog
import com.zxhhyj.music.ui.dialog.EditPlayListTitleDialog
import com.zxhhyj.music.ui.dialog.MediaLibsEmptyDialog
import com.zxhhyj.music.ui.dialog.ScanMusicDialog
import com.zxhhyj.music.ui.dialog.SplashDialog
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.about.AboutScreen
import com.zxhhyj.music.ui.screen.album.AlbumScreen
import com.zxhhyj.music.ui.screen.albumcnt.AlbumCntScreen
import com.zxhhyj.music.ui.screen.hidesong.HiddenSongScreen
import com.zxhhyj.music.ui.screen.lyric.LyricScreen
import com.zxhhyj.music.ui.screen.medialibs.MediaSourceScreen
import com.zxhhyj.music.ui.screen.play.PlayScreen
import com.zxhhyj.music.ui.screen.playlist.PlayListScreen
import com.zxhhyj.music.ui.screen.playlistcnt.PlayListCntScreen
import com.zxhhyj.music.ui.screen.search.SearchScreen
import com.zxhhyj.music.ui.screen.setting.SettingScreen
import com.zxhhyj.music.ui.screen.singer.SingerScreen
import com.zxhhyj.music.ui.screen.singercnt.SingerCntScreen
import com.zxhhyj.music.ui.screen.single.SingleScreen
import com.zxhhyj.music.ui.screen.theme.ThemeScreen
import com.zxhhyj.music.ui.sheet.AddToPlayListSheet
import com.zxhhyj.music.ui.sheet.PlaylistMenuSheet
import com.zxhhyj.music.ui.sheet.SongMenuSheet
import com.zxhhyj.music.ui.sheet.songinfo.SongInfoSheet
import com.zxhhyj.music.ui.theme.appBackgroundColor
import com.zxhhyj.music.ui.theme.round
import com.zxhhyj.music.ui.theme.textColorLight
import com.zxhhyj.music.ui.theme.vertical
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.DialogNavHost
import dev.olshevski.navigation.reimagined.NavAction
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavTransitionSpec
import dev.olshevski.navigation.reimagined.material.BottomSheetNavHost
import dev.olshevski.navigation.reimagined.material.BottomSheetProperties
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController

/**
 * Happy 22nd Birthday Shuangshengzi
 */

@Composable
private fun AppScaffold(
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
                    .background(appBackgroundColor)
            ) {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {

    val mainNavController =
        rememberNavController<ScreenDestination>(startDestination = ScreenDestination.Main)

    val dialogNavController = rememberNavController<DialogDestination>(
        initialBackstack = emptyList()
    )

    val sheetNavController = rememberNavController<BottomSheetDestination>(
        initialBackstack = emptyList()
    )

    var panelState by rememberSaveable {
        mutableStateOf(PanelState.COLLAPSED)
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
        content = { function ->
            val visibilityMediaController by PlayManager.changeMusicLiveData().map {
                return@map it != null
            }.observeAsState(false)
            if (!visibilityMediaController && panelState == PanelState.EXPANDED) {
                function.invoke(PanelState.COLLAPSED)
            }
            AppScaffold(
                modifier = Modifier.fillMaxSize(),
                controllerBar = {
                    AnimatedVisibility(
                        visible = visibilityMediaController,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        MediaController(
                            modifier = Modifier.fillMaxWidth(),
                            panelState = panelState
                        ) {
                            function.invoke(PanelState.EXPANDED)
                        }
                    }
                },
                navigationBar = {
                    if (!visibilityMediaController) {
                        AppDivider(modifier = Modifier.fillMaxWidth())
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
                        function.invoke(PanelState.COLLAPSED)
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
                                SingleScreen(
                                    mainNavController = mainNavController,
                                    sheetNavController = sheetNavController,
                                    dialogNavController = dialogNavController,
                                    padding = padding
                                )
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
                                    padding = padding
                                )
                            }

                            ScreenDestination.Setting -> {
                                SettingScreen(
                                    mainNavController = mainNavController,
                                    padding = padding
                                )
                            }

                            ScreenDestination.Album -> {
                                AlbumScreen(
                                    mainNavController = mainNavController,
                                    padding = padding
                                )
                            }

                            ScreenDestination.MediaLibs -> {
                                MediaSourceScreen(
                                    mainNavController = mainNavController,
                                    dialogNavController = dialogNavController,
                                    padding = padding
                                )
                            }

                            ScreenDestination.Singer -> {
                                SingerScreen(
                                    mainNavController = mainNavController,
                                    padding = padding
                                )
                            }

                            ScreenDestination.PlayList -> {
                                PlayListScreen(
                                    mainNavController = mainNavController,
                                    sheetNavController = sheetNavController,
                                    dialogNavController = dialogNavController,
                                    padding = padding
                                )
                            }

                            ScreenDestination.Theme -> {
                                ThemeScreen(padding = padding)
                            }

                            ScreenDestination.Lyric -> {
                                LyricScreen(padding = padding)
                            }

                            ScreenDestination.HiddenSong -> {
                                HiddenSongScreen(padding = padding)
                            }

                            is ScreenDestination.PlayListCnt -> {
                                PlayListCntScreen(
                                    playlist = destination.model,
                                    sheetNavController = sheetNavController,
                                    padding = padding
                                )
                            }
                        }
                    }
                }
            }
        }) {
        PlayScreen(
            sheetNavController = sheetNavController,
            panelState = panelState,
            it
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
                SplashDialog(onDismissRequest = onDismissRequest)
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
            dialogNavController.pop()
        }
    }

    BottomSheetNavHost(
        controller = sheetNavController,
        onDismissRequest = { sheetNavController.pop() },
        sheetPropertiesSpec = { BottomSheetProperties(skipHalfExpanded = true) }
    ) { destination ->
        BackHandler {
            sheetNavController.pop()
        }
        Column(
            modifier = Modifier
                .background(
                    appBackgroundColor,
                    shape = RoundedCornerShape(topStart = round, topEnd = round)
                )
                .navigationBarsPadding()
                .padding(top = vertical)
        ) {
            Spacer(
                modifier = Modifier
                    .width(50.dp)
                    .height(6.dp)
                    .background(textColorLight.copy(0.3f), RoundedCornerShape(6.dp))
                    .align(Alignment.CenterHorizontally)
            )
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

                is BottomSheetDestination.BottomSheet -> {
                    destination.content()
                }

                is BottomSheetDestination.AddToPlayList -> {
                    AddToPlayListSheet(
                        dialogNavController = dialogNavController,
                        sheetNavController = sheetNavController,
                        song = destination.song
                    )
                }

                is BottomSheetDestination.PlaylistMenu -> {
                    PlaylistMenuSheet(
                        mainNavController = mainNavController,
                        dialogNavController = dialogNavController,
                        sheetNavController = sheetNavController,
                        model = destination.model
                    )
                }
            }
        }

    }
}