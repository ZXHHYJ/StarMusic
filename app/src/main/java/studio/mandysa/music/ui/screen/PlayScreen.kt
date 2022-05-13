package studio.mandysa.music.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.map
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.compose.MandySaMusicKenBurns
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.navHeight

private sealed class PlayNavScreen(
    val route: String,
    @DrawableRes val resourceId: Int
) {
    object CurrentPlay : PlayNavScreen("current_play", R.drawable.ic_round_music_video_24)
    object Lyric : PlayNavScreen("lyric", R.drawable.ic_round_translate_24)
    object PlayList : PlayNavScreen("play_list", R.drawable.ic_round_format_list_bulleted_24)

}

@Composable
fun PlayScreen() {
    val items = listOf(
        PlayNavScreen.CurrentPlay,
        PlayNavScreen.Lyric,
        PlayNavScreen.PlayList
    )
    val navController = rememberNavController()
    Box {
        val coverUrl by PlayManager.changeMusicLiveData().map {
            it.coverUrl
        }.observeAsState("")
        val paused by PlayManager.pauseLiveData().observeAsState(true)
        MandySaMusicKenBurns(
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
            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f),
                navController = navController,
                startDestination = PlayNavScreen.CurrentPlay.route
            ) {
                composable(PlayNavScreen.CurrentPlay.route) {
                    CurrentPlayScreen()
                }
                composable(PlayNavScreen.Lyric.route) {

                }
                composable(PlayNavScreen.PlayList.route) {

                }
            }
            BottomNavigation(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalMargin)
                    .height(navHeight), elevation = 0.dp, backgroundColor = Color.Transparent
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                ImageVector.vectorResource(screen.resourceId),
                                contentDescription = null
                            )
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        selectedContentColor = Color.White,
                        unselectedContentColor = colorResource(R.color.translucent_white),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrentPlayScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(), verticalArrangement = Arrangement.Bottom
    ) {
        val coverUrl by PlayManager.changeMusicLiveData().map { return@map it.coverUrl }
            .observeAsState()
        Card(
            modifier = Modifier
                .size(LocalConfiguration.current.screenWidthDp.dp * 0.8f)
                .align(Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                model = coverUrl,
                modifier = Modifier
                    .fillMaxSize(),
                contentDescription = null
            )
        }
    }
}