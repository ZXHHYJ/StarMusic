package studio.mandysa.music.ui.screen.play

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import studio.mandysa.music.ui.common.*
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.theme.playScreenHorizontal
import studio.mandysa.music.ui.theme.playScreenMaxWidth
import studio.mandysa.music.ui.theme.translucentWhite
import studio.mandysa.music.ui.theme.translucentWhiteFixBug

@Composable
fun NowPlayScreen(sheetNavController: NavController<BottomSheetDestination>) {
    BoxWithPercentages(
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
            if (100.hp > 100.wp) {
                AppCard(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(100.wp),
                    backgroundColor = Color.LightGray,
                    elevation = 10.dp,
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
                AppIcon(
                    Icons.Rounded.MoreVert, null,
                    Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(translucentWhiteFixBug)
                        .clickable {
                            sheetNavController.navigate(BottomSheetDestination.SongMenu(song!!))
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
            val buttonSize = 18.wp

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.hp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val playPauseState by PlayManager.pauseLiveData().map {
                    if (it) R.drawable.ic_play else R.drawable.ic_pause
                }.observeAsState(R.drawable.ic_play)
                AppIcon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_previous),
                    contentDescription = null,
                    modifier = Modifier
                        .size(buttonSize)
                        .clickable {
                            PlayManager.skipToPrevious()
                        },
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.wp))
                AppIcon(
                    imageVector = ImageVector.vectorResource(id = playPauseState),
                    contentDescription = null,
                    modifier = Modifier
                        .scale(1.2f)
                        .size(23.wp)
                        .clickable {
                            if (PlayManager.isPaused())
                                PlayManager.play()
                            else PlayManager.pause()
                        },
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.wp))
                AppIcon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_next),
                    contentDescription = null,
                    modifier = Modifier
                        .size(buttonSize)
                        .clickable {
                            PlayManager.skipToNext()
                        },
                    tint = Color.White
                )
            }
        }
    }
}


