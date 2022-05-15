package studio.mandysa.music.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.github.krottv.compose.sliders.DefaultThumb
import com.github.krottv.compose.sliders.DefaultTrack
import com.github.krottv.compose.sliders.SliderValueHorizontal
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.compose.MandySaMusicKenBurns
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.translucentWhite
import studio.mandysa.music.ui.theme.verticalMargin

private sealed class PlayNavScreen(
    val route: String
) {
    object CurrentPlay : PlayNavScreen("current_play")
    object Lyric : PlayNavScreen("lyric")
    object PlayList : PlayNavScreen("play_list")
}

@Composable
fun PlayScreen() {
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
            LazyColumn(modifier = Modifier.fillMaxWidth()) {

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
        Card(
            modifier = Modifier
                .size(LocalConfiguration.current.screenWidthDp.dp * 0.8f)
                .align(Alignment.CenterHorizontally)
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
        Spacer(modifier = Modifier.height(15.dp))
        Column(
            modifier = Modifier.padding(vertical = verticalMargin),
            horizontalAlignment = Alignment.CenterHorizontally
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
        MusicProgressBar()
        MusicControlBar()
    }
}

@Composable
private fun MusicProgressBar() {
    val musicPlaybackProgress by PlayManager.playingMusicProgressLiveData().map {
        it.toFloat()
    }.observeAsState(0f)
    val musicDuration by PlayManager.playingMusicDurationLiveData().map {
        it.toFloat()
    }.observeAsState(1f)
    SliderValueHorizontal(
        musicPlaybackProgress, { PlayManager.seekTo(it.toInt()) },
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .padding(horizontal = 35.dp),
        valueRange = 0f..musicDuration,
        thumbHeightMax = true,
        track = { modifier: Modifier,
                  fraction: Float,
                  interactionSource: MutableInteractionSource,
                  tickFractions: List<Float>,
                  enabled: Boolean ->
            DefaultTrack(
                modifier,
                fraction,
                interactionSource,
                tickFractions,
                enabled,
                height = 4.dp, colorProgress = Color.White, colorTrack = translucentWhite
            )
        },
        thumb = { modifier: Modifier,
                  offset: Dp,
                  interactionSource: MutableInteractionSource,
                  enabled: Boolean,
                  thumbSize: DpSize ->
            DefaultThumb(
                modifier, offset, interactionSource, enabled, thumbSize,
                color = Color.White,
                scaleOnPress = 1.3f
            )
        }
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

private fun Float.toTime(): String {
    val s: Int = this.toInt() / 1000
    return (s / 60).toString() + ":" + s % 60
}

@Composable
private fun MusicControlBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val playPauseState by PlayManager.pauseLiveData().switchMap {
            MutableLiveData<Int>().apply {
                value = if (it) R.drawable.ic_play else R.drawable.ic_pause
            }
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