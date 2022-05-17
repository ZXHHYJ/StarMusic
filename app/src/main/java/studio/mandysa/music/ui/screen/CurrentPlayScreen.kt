package studio.mandysa.music.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import coil.compose.AsyncImage
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.SeekBar
import studio.mandysa.music.ui.theme.round
import studio.mandysa.music.ui.theme.translucentWhite
import studio.mandysa.music.ui.theme.verticalMargin

@Composable
fun CurrentPlayScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AlbumCover()
        TitleAndArtist()
        Spacer(modifier = Modifier.height(15.dp))
        MusicProgressBar()
        MusicControlBar()
    }
}


@Composable
private fun AlbumCover() {
    Card(
        modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp * 0.8f),
        elevation = 5.dp,
        shape = RoundedCornerShape(round)
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

@Composable
private fun MusicControlBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val playPauseState by PlayManager.pauseLiveData().map {
            if (it) R.drawable.ic_play else R.drawable.ic_pause
        }.observeAsState(R.drawable.ic_play)
        Icon(
            modifier = Modifier
                .size(60.dp)
                .clickable {
                    PlayManager.skipToPrevious()
                },
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_previous),
            tint = Color.White,
            contentDescription = null
        )
        Box(modifier = Modifier.padding(horizontal = 35.dp)) {
            Icon(
                modifier = Modifier
                    .size(85.dp)
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
                .size(60.dp)
                .clickable {
                    PlayManager.skipToNext()
                },
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_next),
            tint = Color.White,
            contentDescription = null
        )
    }
}