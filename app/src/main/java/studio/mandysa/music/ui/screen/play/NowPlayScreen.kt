package studio.mandysa.music.ui.screen.play

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.map
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.ktx.artist
import studio.mandysa.music.service.playmanager.ktx.coverUrl
import studio.mandysa.music.service.playmanager.ktx.title
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.ScreenLayout
import studio.mandysa.music.ui.common.SeekBar
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.theme.roundedCornerShape
import studio.mandysa.music.ui.theme.translucentWhite
import studio.mandysa.music.ui.theme.translucentWhiteFixBug

@Composable
fun NowPlayScreen(dialogNavController: NavController<DialogDestination>) {
    ScreenLayout(
        modifier = Modifier
            .widthIn(max = playScreenMaxWidth)
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = playScreenHorizontal)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (100.hpp > 100.wpp) {
                Card(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(100.wpp),
                    elevation = 10.dp,
                    shape = roundedCornerShape
                ) {
                    val coverUrl by PlayManager.changeMusicLiveData().map { return@map it.coverUrl }
                        .observeAsState()
                    coverUrl?.let {
                        AppAsyncImage(modifier = Modifier.fillMaxSize(), url = it)
                    }
                }
            }
            //-----
            Row(
                modifier = Modifier
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.0f),
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
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.padding(top = 2.dp))
                    Text(
                        text = musician,
                        color = translucentWhite,
                        fontSize = 16.sp,
                        maxLines = 1
                    )
                }
                val song by PlayManager.changeMusicLiveData().observeAsState()
                Icon(
                    Icons.Rounded.MoreVert, null,
                    Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(translucentWhiteFixBug)
                        .clickable {
                            dialogNavController.navigate(DialogDestination.SongMenu(song!!))
                        },
                    tint = Color.White
                )
            }
            //-----
            fun Int.toTime(): String {
                val s: Int = this / 1000
                return (s / 60).toString() + ":" + s % 60
            }

            val musicPlaybackProgress by PlayManager.playingMusicProgressLiveData().map {
                it
            }.observeAsState(0)
            val musicDuration by PlayManager.playingMusicDurationLiveData().map {
                it
            }.observeAsState(1)

            SeekBar(
                modifier = Modifier
                    .fillMaxWidth(),
                value = musicPlaybackProgress,
                maxValue = musicDuration,
                onValueChange = { PlayManager.seekTo(it) }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = musicPlaybackProgress.toTime(), color = translucentWhite)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = musicDuration.toTime(), color = translucentWhite)
            }
            //-----
            val smallButtonPadding = 2.wpp
            val middleButtonSize = 24.wpp

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.hpp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val playPauseState by PlayManager.pauseLiveData().map {
                    if (it) R.drawable.ic_play else R.drawable.ic_pause
                }.observeAsState(R.drawable.ic_play)
                Icon(
                    ImageVector.vectorResource(id = R.drawable.ic_skip_previous), null,
                    Modifier
                        .size(middleButtonSize)
                        .padding(smallButtonPadding)
                        .clip(RoundedCornerShape(middleButtonSize))
                        .clickable {
                            PlayManager.skipToPrevious()
                        },
                    tint = Color.White
                )
                Box(modifier = Modifier.padding(horizontal = 6.wpp)) {
                    Icon(
                        ImageVector.vectorResource(id = playPauseState), null,
                        Modifier
                            .size(middleButtonSize)
                            .clip(RoundedCornerShape(middleButtonSize))
                            .clickable {
                                if (PlayManager.isPaused())
                                    PlayManager.play()
                                else PlayManager.pause()
                            },
                        tint = Color.White
                    )
                }
                Icon(
                    ImageVector.vectorResource(id = R.drawable.ic_skip_next), null,
                    Modifier
                        .size(middleButtonSize)
                        .padding(smallButtonPadding)
                        .clip(RoundedCornerShape(middleButtonSize))
                        .clickable {
                            PlayManager.skipToNext()
                        },
                    tint = Color.White
                )
            }
        }
    }
}


