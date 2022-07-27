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
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.SeekBar
import studio.mandysa.music.ui.theme.isMedium
import studio.mandysa.music.ui.theme.translucentWhite
import studio.mandysa.music.ui.theme.translucentWhiteFixBug
import studio.mandysa.music.ui.theme.verticalMargin

@Composable
fun NowPlayScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isMedium) {
            AlbumCover()
            TitleAndArtist()
            MusicProgressBar()
            MusicControlBar()
        } else {
            AlbumCover()
            Spacer(modifier = Modifier.height(10.dp))
            TitleAndArtist()
            Spacer(modifier = Modifier.height(15.dp))
            MusicProgressBar()
            MusicControlBar()
        }
    }
}


@Composable
private fun AlbumCover() {
    Card(
        modifier = Modifier
            .widthIn(max = maxWidth)
            .heightIn(max = maxWidth),
        elevation = 5.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        val coverUrl by PlayManager.changeMusicLiveData().map { return@map it.coverUrl }
            .observeAsState()
        coverUrl?.let {
            AppAsyncImage(size = maxWidth, url = it)
        }
    }
}

@Composable
private fun TitleAndArtist() {
    Row(
        modifier = Modifier
            .widthIn(max = maxWidth)
            .fillMaxWidth()
            .padding(vertical = verticalMargin), verticalAlignment = Alignment.CenterVertically
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
        Icon(
            Icons.Rounded.MoreVert, null,
            Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(translucentWhiteFixBug)
                .clickable {

                },
            tint = Color.White
        )
    }
}

@Composable
private fun MusicProgressBar() {
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
            .widthIn(max = maxWidth)
            .fillMaxWidth(),
        value = musicPlaybackProgress,
        maxValue = musicDuration,
        onValueChange = { PlayManager.seekTo(it) }
    )
    Row(
        modifier = Modifier
            .widthIn(max = maxWidth)
            .fillMaxWidth()
    ) {
        Text(text = musicPlaybackProgress.toTime(), color = translucentWhite)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = musicDuration.toTime(), color = translucentWhite)
    }
}

@Composable
private fun MusicControlBar() {
    val smallButtonSize = if (isMedium) 55.dp else 65.dp
    val middleButtonSize = if (isMedium) 65.dp else 85.dp

    Row(
        modifier = Modifier
            .widthIn(max = maxWidth)
            .fillMaxWidth()
            .padding(vertical = if (isMedium) 15.dp else 50.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val playPauseState by PlayManager.pauseLiveData().map {
            if (it) R.drawable.ic_play else R.drawable.ic_pause
        }.observeAsState(R.drawable.ic_play)
        Icon(
            ImageVector.vectorResource(id = R.drawable.ic_skip_previous), null,
            Modifier
                .size(smallButtonSize)
                .clip(RoundedCornerShape(smallButtonSize))
                .clickable {
                    PlayManager.skipToPrevious()
                },
            tint = Color.White
        )
        Box(modifier = Modifier.padding(horizontal = 35.dp)) {
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
                .size(smallButtonSize)
                .clip(RoundedCornerShape(smallButtonSize))
                .clickable {
                    PlayManager.skipToNext()
                },
            tint = Color.White
        )
    }
}