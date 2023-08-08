package com.zxhhyj.music.ui.screen.play

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FontDownload
import androidx.compose.material.icons.rounded.FormatListBulleted
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.map
import com.mxalbert.sharedelements.*
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.coverUrl
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.common.AlbumCoverGradient
import com.zxhhyj.music.ui.common.BoxWithPercentages
import com.zxhhyj.music.ui.common.ImageMotionBlur
import com.zxhhyj.music.ui.common.PanelController
import com.zxhhyj.music.ui.common.PanelState
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.play.common.TopMediaController
import com.zxhhyj.music.ui.theme.*
import dev.olshevski.navigation.reimagined.*


enum class PlayScreenDestination {
    Main,
    Lyric,
    PlayQueue,
}

val PlayScreenDestination.tabIcon
    get() = when (this) {
        PlayScreenDestination.Main -> throw RuntimeException()
        PlayScreenDestination.Lyric -> Icons.Rounded.FontDownload
        PlayScreenDestination.PlayQueue -> Icons.Rounded.FormatListBulleted
    }

const val ShareAlbumKey = "album"

const val AnimDurationMillis = 300

object PlayScreen {
    val PlayScreenContentHorizontal = 10.dp
    val PlayScreenHorizontal = 20.dp
    val PlayScreenMaxWidth = 360.dp
}

val MaterialFadeInTransitionSpec = SharedElementsTransitionSpec(
    pathMotionFactory = MaterialArcMotionFactory,
    durationMillis = AnimDurationMillis,
    fadeMode = FadeMode.In
)

val MaterialFadeOutTransitionSpec = SharedElementsTransitionSpec(
    pathMotionFactory = MaterialArcMotionFactory,
    durationMillis = AnimDurationMillis,
    fadeMode = FadeMode.Out
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayScreen(
    sheetNavController: NavController<BottomSheetDestination>,
    panelController: PanelController
) {

    val navController = rememberNavController(startDestination = PlayScreenDestination.Main)

    val lastDestination = navController.backstack.entries.last().destination

    val coverUrl by PlayManager.currentSongLiveData().map {
        it?.album?.coverUrl
    }.observeAsState()

    if (panelController.panelState == PanelState.COLLAPSED) {
        navController.popUpTo {
            it == PlayScreenDestination.Main
        }
    }

    SharedElementsRoot {
        BoxWithPercentages(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {

            @Composable
            fun BottomNavigationBar() {
                BottomNavigation(
                    modifier = Modifier
                        .widthIn(max = PlayScreen.PlayScreenMaxWidth)
                        .fillMaxWidth()
                        .padding(horizontal = PlayScreen.PlayScreenContentHorizontal + PlayScreen.PlayScreenHorizontal),
                    elevation = 0.dp,
                    backgroundColor = Color.Transparent
                ) {
                    listOf(
                        PlayScreenDestination.Lyric,
                        PlayScreenDestination.PlayQueue
                    ).forEach { screen ->
                        val selected = screen == lastDestination
                        BottomNavigationItem(modifier = Modifier.clip(RoundedCornerShape(round)),
                            icon = { Icon(screen.tabIcon, contentDescription = screen.name) },
                            selectedContentColor = Color.White,
                            unselectedContentColor = translucentWhiteColor,
                            selected = selected,
                            onClick = {
                                if (selected) {
                                    navController.popUpTo {
                                        it == PlayScreenDestination.Main
                                    }
                                    return@BottomNavigationItem
                                }
                                if (!navController.moveToTop { it == screen }) {
                                    navController.navigate(screen)
                                }
                            }
                        )
                    }
                }
            }

            @Composable
            fun MainNavHost() {
                val visibilityTopMediaBar = maxWidth < maxHeight
                Box(
                    modifier = Modifier
                        .widthIn(max = PlayScreen.PlayScreenMaxWidth)
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedNavHost(
                        controller = navController,
                        transitionSpec = { _, _, _ ->
                            val tween = tween<Float>(durationMillis = AnimDurationMillis)
                            fadeIn(tween) with fadeOut(tween)
                        }
                    ) {
                        when (it) {
                            PlayScreenDestination.Main -> {
                                NowPlayScreen(sheetNavController)
                            }

                            PlayScreenDestination.Lyric -> {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    if (visibilityTopMediaBar) {
                                        LazyColumn(userScrollEnabled = false) {
                                            item {
                                                TopMediaController(
                                                    navController = navController,
                                                    sheetNavController = sheetNavController
                                                )
                                            }
                                        }
                                    }
                                    PlayLyricScreen()
                                }
                            }

                            PlayScreenDestination.PlayQueue -> {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    if (visibilityTopMediaBar) {
                                        LazyColumn(userScrollEnabled = false) {
                                            item {
                                                TopMediaController(
                                                    navController = navController,
                                                    sheetNavController = sheetNavController
                                                )
                                            }
                                        }
                                    }
                                    PlayQueueScreen()
                                }
                            }
                        }
                    }
                }
            }

            ImageMotionBlur(
                modifier = Modifier.fillMaxSize(),
                imageUrl = coverUrl,
                paused = panelController.panelState == PanelState.COLLAPSED
            )

            if (SettingRepository.EnableNewPlayerUI) {
                val tween = tween<Float>(durationMillis = AnimDurationMillis)
                val enter = fadeIn(tween)
                val exit = fadeOut(tween)
                val isMainScreen = lastDestination == PlayScreenDestination.Main
                AnimatedVisibility(
                    visible = isMainScreen,
                    enter = enter,
                    exit = exit
                ) {
                    AlbumCoverGradient(modifier = Modifier.fillMaxSize(), url = coverUrl)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BackHandler(panelController.panelState == PanelState.EXPANDED) {
                    panelController.swipeTo(PanelState.COLLAPSED)
                }
                when {
                    maxWidth >= maxHeight -> {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Spacer(modifier = Modifier.width(12.wp))
                            Column(
                                modifier = Modifier
                                    .width(35.wp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1.0f)
                                ) {
                                    NowPlayScreen(sheetNavController)
                                }
                                BottomNavigationBar()
                            }
                            Spacer(modifier = Modifier.width(6.wp))
                            if (lastDestination == PlayScreenDestination.Main) {
                                navController.navigate(PlayScreenDestination.Lyric)
                            }
                            MainNavHost()
                            Spacer(modifier = Modifier.width(12.wp))
                        }
                    }

                    else -> {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1.0f),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            BackHandler(lastDestination != PlayScreenDestination.Main) {
                                navController.popUpTo { it == PlayScreenDestination.Main }
                            }
                            MainNavHost()
                        }
                        BottomNavigationBar()
                    }
                }
            }
        }
    }
}

