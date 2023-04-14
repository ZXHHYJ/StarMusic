package com.zxhhyj.music.ui.screen.play

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.ktx.coverUrl
import com.zxhhyj.music.ui.composable.BoxWithPercentages
import com.zxhhyj.music.ui.composable.BoxWithPercentagesScope
import com.zxhhyj.music.ui.composable.MotionBlur
import com.zxhhyj.music.ui.composable.PanelState
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.play.composable.TopMediaController
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

private const val TransitionDurationMillis = 250

val MaterialFadeInTransitionSpec = SharedElementsTransitionSpec(
    pathMotionFactory = MaterialArcMotionFactory,
    durationMillis = TransitionDurationMillis,
    fadeMode = FadeMode.In
)

val MaterialFadeOutTransitionSpec = SharedElementsTransitionSpec(
    pathMotionFactory = MaterialArcMotionFactory,
    durationMillis = TransitionDurationMillis,
    fadeMode = FadeMode.Out
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayScreen(
    sheetNavController: NavController<BottomSheetDestination>,
    panelState: PanelState?,
    function: (PanelState) -> Unit
) {

    val navController = rememberNavController(startDestination = PlayScreenDestination.Main)

    if (panelState == PanelState.COLLAPSED) {
        navController.popUpTo {
            it == PlayScreenDestination.Main
        }
    }

    @Composable
    fun BottomNavigationBar() {
        val scope = LocalSharedElementsRootScope.current!!
        BottomNavigation(
            modifier = Modifier
                .widthIn(max = playScreenMaxWidth)
                .fillMaxWidth()
                .padding(horizontal = horizontal),
            elevation = 0.dp,
            backgroundColor = Color.Transparent
        ) {
            val lastDestination = navController.backstack.entries.last().destination
            listOf(
                PlayScreenDestination.Lyric,
                PlayScreenDestination.PlayQueue
            ).forEach { screen ->
                val selected = screen == lastDestination
                BottomNavigationItem(modifier = Modifier.clip(roundShape),
                    icon = {
                        Icon(
                            screen.tabIcon,
                            contentDescription = null
                        )
                    },
                    selectedContentColor = Color.White,
                    unselectedContentColor = translucentWhite,
                    selected = selected,
                    onClick = {
                        if (selected) {
                            navController.popUpTo {
                                it == PlayScreenDestination.Main
                            }
                        } else
                            if (!navController.moveToTop { it == screen }) {
                                navController.navigate(screen)
                            }
                        scope.prepareTransition(screen)
                    }
                )
            }
        }
    }

    @Composable
    fun BoxWithPercentagesScope.RightNavHost() {
        val visibilityTopMediaBar = maxWidth < maxHeight
        Box(
            modifier = Modifier
                .widthIn(max = playScreenMaxWidth), contentAlignment = Alignment.Center
        ) {
            AnimatedNavHost(
                controller = navController,
                transitionSpec = { _, _, _ ->
                    val tween = tween<Float>(durationMillis = TransitionDurationMillis)
                    fadeIn(tween) with fadeOut(tween)
                }
            ) {
                when (it) {
                    PlayScreenDestination.Main -> {
                        NowPlayScreen(sheetNavController)
                    }
                    PlayScreenDestination.Lyric -> {
                        Column {
                            if (visibilityTopMediaBar) {
                                LazyColumn {
                                    item {
                                        TopMediaController(
                                            navController = navController,
                                            sheetNavController = sheetNavController
                                        )
                                    }
                                }
                            }
                            LyricScreen()
                        }

                    }
                    PlayScreenDestination.PlayQueue -> {
                        Column {
                            if (visibilityTopMediaBar) {
                                LazyColumn {
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

    SharedElementsRoot {
        BoxWithPercentages(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {

            val coverUrl by PlayManager.changeMusicLiveData().map {
                it?.coverUrl
            }.observeAsState()
            MotionBlur(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
                url = coverUrl,
                paused = panelState == PanelState.COLLAPSED
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    maxWidth >= maxHeight -> {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            BackHandler(panelState == PanelState.EXPANDED) {
                                function.invoke(PanelState.COLLAPSED)
                            }
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
                            val lastDestination = navController.backstack.entries.last().destination
                            if (lastDestination == PlayScreenDestination.Main) {
                                navController.navigate(PlayScreenDestination.Lyric)
                            }
                            RightNavHost()
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
                            BackHandler(panelState == PanelState.EXPANDED) {
                                function.invoke(PanelState.COLLAPSED)
                            }
                            NavBackHandler(navController)
                            RightNavHost()
                        }
                        BottomNavigationBar()
                    }
                }
            }
        }
    }
}

