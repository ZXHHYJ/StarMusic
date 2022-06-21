package studio.mandysa.music.ui.screen.play

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.map
import dev.olshevski.navigation.reimagined.*
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.KenBurns
import studio.mandysa.music.ui.common.PanelState
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.navHeight
import studio.mandysa.music.ui.theme.translucentWhite
import studio.mandysa.music.ui.theme.verticalMargin

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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayScreen(panelState: PanelState?, function: (PanelState) -> Unit) {
    val navController = rememberNavController(startDestination = PlayScreenDestination.Main)
    Box {
        val coverUrl by PlayManager.changeMusicLiveData().map {
            it.coverUrl
        }.observeAsState("")
        KenBurns(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray),
            imageUrl = coverUrl,
            paused = panelState == PanelState.COLLAPSED
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = verticalMargin)
                    .align(Alignment.CenterHorizontally)
            ) {
                Spacer(
                    modifier = Modifier
                        .width(40.dp)
                        .height(5.dp)
                        .background(shape = RoundedCornerShape(5.dp), color = translucentWhite)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
            ) {
                BackHandler(panelState == PanelState.EXPANDED) {
                    function.invoke(PanelState.COLLAPSED)
                }
                NavBackHandler(navController)
                //处理返回逻辑
                AnimatedNavHost(navController) {
                    when (it) {
                        PlayScreenDestination.Main -> {
                            NowPlayScreen()
                        }
                        PlayScreenDestination.Lyric -> {
                            LyricScreen()
                        }
                        PlayScreenDestination.PlayQueue -> {
                            PlayQueueScreen()
                        }
                    }
                }
            }
            BottomNavigation(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(navHeight)
                    .padding(horizontal = horizontalMargin),
                elevation = 0.dp,
                backgroundColor = Color.Transparent
            ) {
                val lastDestination = navController.backstack.entries.last().destination
                listOf(
                    PlayScreenDestination.Lyric,
                    PlayScreenDestination.PlayQueue
                ).forEach { screen ->
                    val selected = screen == lastDestination
                    BottomNavigationItem(
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
    }
}
