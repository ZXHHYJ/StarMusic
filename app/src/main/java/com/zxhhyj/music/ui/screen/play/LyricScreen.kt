package com.zxhhyj.music.ui.screen.play

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.ui.composable.Lyric
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.playScreenHorizontal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File

@Composable
fun ColumnScope.LyricScreen() {
    val song by PlayManager.changeMusicLiveData().observeAsState()
    var lyric by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(song) {
        when (song) {
            is SongBean.Local -> {
                launch(Dispatchers.IO) {
                    val model = (song as SongBean.Local)
                    lyric = try {
                        val audioFile = AudioFileIO.read(File(model.data))
                        audioFile.tag.getFirst(FieldKey.LYRICS)
                    } catch (_: Exception) {
                        String()
                    }
                }
            }
            is SongBean.Network -> {}
            null -> {}
        }
    }
    if (lyric.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            Text(
                text = stringResource(id = R.string.not_lyric),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        val liveTime by PlayManager.playingMusicProgressLiveData()
            .observeAsState()
        liveTime?.let { time ->
            Lyric(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .padding(horizontal = playScreenHorizontal - horizontal / 2),
                lyric = lyric,
                liveTime = time
            ) {
                PlayManager.seekTo(it)
            }
        }
    }

}