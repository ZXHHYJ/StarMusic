package com.zxhhyj.music.ui.screen.play

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import com.mxalbert.sharedelements.FadeMode
import com.mxalbert.sharedelements.MaterialArcMotionFactory
import com.mxalbert.sharedelements.SharedElementsRoot
import com.mxalbert.sharedelements.SharedElementsTransitionSpec
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.AlbumMotionBlur
import com.zxhhyj.music.ui.common.BoxWithPercentages
import com.zxhhyj.music.ui.common.PanelController
import com.zxhhyj.music.ui.common.PanelState
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.screen.play.common.TopMediaController
import com.zxhhyj.music.ui.theme.round
import com.zxhhyj.music.ui.theme.translucentWhiteColor
import com.zxhhyj.ui.theme.StarDimens
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.moveToTop
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popUpTo
import dev.olshevski.navigation.reimagined.rememberNavController


enum class PlayScreenDestination {
    Controller,
    Lyric,
    PlayQueue,
}

val PlayScreenDestination.tabIcon
    get() = when (this) {
        PlayScreenDestination.Controller -> throw RuntimeException()
        PlayScreenDestination.Lyric -> Icons.Rounded.FontDownload
        PlayScreenDestination.PlayQueue -> Icons.Rounded.FormatListBulleted
    }

const val ShareAlbumKey = "album"

val AnimDurationMillis
    get() = if (!SettingRepository.EnableLinkUI) 300 else 0

object PlayScreen {
    val PlayScreenContentHorizontal = StarDimens.horizontal
    val PlayScreenHorizontal = 12.dp
}

val MaterialFadeInTransitionSpec
    get() = SharedElementsTransitionSpec(
        pathMotionFactory = MaterialArcMotionFactory,
        durationMillis = AnimDurationMillis,
        fadeMode = FadeMode.In
    )

val MaterialFadeOutTransitionSpec
    get() = SharedElementsTransitionSpec(
        pathMotionFactory = MaterialArcMotionFactory,
        durationMillis = AnimDurationMillis,
        fadeMode = FadeMode.Out
    )

@Composable
fun PlayScreen(
    sheetNavController: NavController<SheetDestination>,
    panelController: PanelController
) {

    val navController = rememberNavController(startDestination = PlayScreenDestination.Controller)

    val lastDestination = navController.backstack.entries.last().destination

    val coverUrl by PlayerManager.currentSongLiveData().map {
        it?.coverUrl
    }.observeAsState()

    if (panelController.panelState == PanelState.COLLAPSED) {
        navController.popUpTo {
            it == PlayScreenDestination.Controller
        }
    }

    SharedElementsRoot {
        BoxWithPercentages(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {

            @Composable
            fun BottomNavigationBar(modifier: Modifier) {
                BottomNavigation(
                    modifier = modifier.height(60.dp),
                    elevation = 0.dp,
                    backgroundColor = Color.Transparent
                ) {
                    listOf(
                        PlayScreenDestination.Lyric,
                        PlayScreenDestination.PlayQueue
                    ).forEach { screen ->
                        val selected = screen == lastDestination
                        BottomNavigationItem(
                            modifier = Modifier.clip(RoundedCornerShape(round)),
                            icon = { Icon(screen.tabIcon, contentDescription = screen.name) },
                            selectedContentColor = Color.White,
                            unselectedContentColor = translucentWhiteColor,
                            selected = selected,
                            onClick = {
                                if (selected) {
                                    navController.popUpTo {
                                        it == PlayScreenDestination.Controller
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
                AnimatedNavHost(
                    controller = navController,
                    transitionSpec = { _, _, _ ->
                        if (SettingRepository.EnableLinkUI) {
                            return@AnimatedNavHost EnterTransition.None togetherWith ExitTransition.None
                        }
                        val tween = tween<Float>(durationMillis = AnimDurationMillis)
                        fadeIn(tween) togetherWith fadeOut(tween)
                    }
                ) {
                    when (it) {
                        PlayScreenDestination.Controller -> {
                            PlayControllerScreen(sheetNavController)
                        }

                        else -> {
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
                                when (it) {
                                    PlayScreenDestination.Lyric -> PlayLyricScreen()
                                    PlayScreenDestination.PlayQueue -> PlayQueueScreen()
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }

            when (SettingRepository.EnableLinkUI) {
                true -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.DarkGray)
                    )
                }

                false -> {
                    AlbumMotionBlur(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.DarkGray),
                        albumUrl = coverUrl,
                        paused = panelController.panelState == PanelState.COLLAPSED
                    )
                }
            }

            BackHandler(panelController.panelState == PanelState.EXPANDED) {
                panelController.swipeTo(PanelState.COLLAPSED)
            }
            when {
                maxWidth >= maxHeight -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding()
                    ) {
                        Spacer(modifier = Modifier.width(12.wp))
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1.0f)
                                .padding(horizontal = PlayScreen.PlayScreenHorizontal),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1.0f)
                            ) {
                                PlayControllerScreen(sheetNavController)
                            }
                            BottomNavigationBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = PlayScreen.PlayScreenContentHorizontal)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.wp))
                        if (lastDestination == PlayScreenDestination.Controller) {
                            navController.navigate(PlayScreenDestination.Lyric)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1.0f)
                                .padding(horizontal = PlayScreen.PlayScreenHorizontal)
                        ) {
                            MainNavHost()
                        }
                        Spacer(modifier = Modifier.width(12.wp))
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding()
                            .padding(horizontal = PlayScreen.PlayScreenHorizontal),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1.0f),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            BackHandler(lastDestination != PlayScreenDestination.Controller) {
                                navController.popUpTo { it == PlayScreenDestination.Controller }
                            }
                            MainNavHost()
                        }
                        BottomNavigationBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = PlayScreen.PlayScreenContentHorizontal)
                        )
                    }
                }
            }
        }
    }
}

