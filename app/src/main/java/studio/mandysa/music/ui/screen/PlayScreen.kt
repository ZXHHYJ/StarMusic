package studio.mandysa.music.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Abc
import androidx.compose.material.icons.rounded.FontDownload
import androidx.compose.material.icons.rounded.FormatListBulleted
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import studio.mandysa.music.ui.compose.KenBurns
import studio.mandysa.music.ui.compose.SeekBar
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.navHeight
import studio.mandysa.music.ui.theme.translucentWhite
import studio.mandysa.music.ui.theme.verticalMargin

private sealed class PlayNavScreen(
    val route: String, val vector: ImageVector
) {
    object CurrentPlay : PlayNavScreen("current_play", Icons.Rounded.Abc)
    object Lyric : PlayNavScreen("lyric", Icons.Rounded.FontDownload)
    object PlayList : PlayNavScreen("play_list", Icons.Rounded.FormatListBulleted)
}

@Composable
fun PlayScreen() {
    val items = listOf(PlayNavScreen.Lyric, PlayNavScreen.PlayList)
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

                }
                composable(PlayNavScreen.PlayList.route) {

                }
            }
            BottomNavigation(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(navHeight)
                    .padding(horizontal = horizontalMargin),
                elevation = 0.dp,
                backgroundColor = Color.Transparent, contentColor = Color.White
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            androidx.compose.material3.Icon(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumCover() {
    Card(
        modifier = Modifier
            .size(LocalConfiguration.current.screenWidthDp.dp * 0.8f)
    ) {
        val coverUrl by PlayManager.changeMusicLiveData().map { return@map it.coverUrl }
            .observeAsState()
        AsyncImage(
            model = coverUrl,
            modifier = Modifier
                .fillMaxSize(),
            contentDescription = null
        )
    }
}

@Composable
private fun CurrentPlayScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.padding(vertical = verticalMargin)) {
            Spacer(
                modifier = Modifier
                    .width(40.dp)
                    .height(5.dp)
                    .background(shape = RoundedCornerShape(5.dp), color = translucentWhite)
            )
        }
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AlbumCover()
            TitleAndArtist()
        }
        Spacer(modifier = Modifier.height(15.dp))
        MusicProgressBar()
        MusicControlBar()
    }
}

@Composable
private fun TitleAndArtist() {
    Column(
        modifier = Modifier
            .padding(vertical = verticalMargin, horizontal = 35.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        val title by PlayManager.changeMusicLiveData().map { return@map it.title }
            .observeAsState("")
        val musician by PlayManager.changeMusicLiveData().map {
            it.artist[0].name
        }.observeAsState("")
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(top = 2.dp))
        Text(
            text = musician,
            color = translucentWhite,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun MusicProgressBar() {
    val musicPlaybackProgress by PlayManager.playingMusicProgressLiveData().map {
        it
    }.observeAsState(0)
    val musicDuration by PlayManager.playingMusicDurationLiveData().map {
        it
    }.observeAsState(1)
    SeekBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp),
        value = musicPlaybackProgress,
        maxValue = musicDuration,
        onValueChange = { PlayManager.seekTo(it) }
    )
    Row(
        modifier = Modifier
            .padding(horizontal = 35.dp)
            .fillMaxWidth()
    ) {
        Text(text = musicPlaybackProgress.toTime(), color = translucentWhite)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = musicDuration.toTime(), color = translucentWhite)
    }
}

private fun Int.toTime(): String {
    val s: Int = this / 1000
    return (s / 60).toString() + ":" + s % 60
}

@Composable
private fun MusicControlBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val playPauseState by PlayManager.pauseLiveData().map {
            if (it) R.drawable.ic_play else R.drawable.ic_pause
        }.observeAsState(R.drawable.ic_play)
        Icon(
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    PlayManager.skipToPrevious()
                },
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_previous),
            tint = Color.White,
            contentDescription = null
        )
        Box(modifier = Modifier.padding(horizontal = horizontalMargin)) {
            Icon(
                modifier = Modifier
                    .size(70.dp)
                    .clickable {
                        if (PlayManager.pauseLiveData().value == true)
                            PlayManager.play()
                        else PlayManager.pause()
                    },
                imageVector = ImageVector.vectorResource(id = playPauseState),
                tint = Color.White,
                contentDescription = null
            )
        }
        Icon(
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    PlayManager.skipToNext()
                },
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_next),
            tint = Color.White,
            contentDescription = null
        )
    }
}