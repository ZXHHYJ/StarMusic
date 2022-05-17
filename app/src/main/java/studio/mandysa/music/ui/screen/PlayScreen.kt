package studio.mandysa.music.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Abc
import androidx.compose.material.icons.rounded.FontDownload
import androidx.compose.material.icons.rounded.FormatListBulleted
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.map
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.KenBurns
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.navHeight
import studio.mandysa.music.ui.theme.translucentWhite
import studio.mandysa.music.ui.theme.verticalMargin

private sealed class PlayNavScreen(
    val route: String, val vector: ImageVector
) {
    object CurrentPlay : PlayNavScreen("current_play", Icons.Rounded.Abc)
    object Lyric : PlayNavScreen("lyric", Icons.Rounded.FontDownload)
    object PlayQueue : PlayNavScreen("play_queue", Icons.Rounded.FormatListBulleted)
}

@Composable
fun PlayScreen() {
    val items = listOf(PlayNavScreen.Lyric, PlayNavScreen.PlayQueue)
    val navController = rememberNavController()
    Box {
        val coverUrl by PlayManager.changeMusicLiveData().map {
            it.coverUrl
        }.observeAsState("")
        val paused by PlayManager.pauseLiveData().observeAsState(true)
        KenBurns(
            modifier = Modifier.fillMaxSize(),
            imageUrl = coverUrl,
            paused = paused
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
            NavHost(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                navController = navController,
                startDestination = PlayNavScreen.CurrentPlay.route
            ) {
                composable(PlayNavScreen.CurrentPlay.route) {
                    CurrentPlayScreen()
                }
                composable(PlayNavScreen.Lyric.route) {
                    LyricScreen()
                }
                composable(PlayNavScreen.PlayQueue.route) {
                    PlayQueueScreen()
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
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                screen.vector,
                                contentDescription = null
                            )
                        },
                        selectedContentColor = Color.White,
                        unselectedContentColor = translucentWhite,
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}
