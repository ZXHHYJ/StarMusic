package studio.mandysa.music.ui.screen.play

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.ui.common.Lyric
import studio.mandysa.music.ui.theme.horizontal
import studio.mandysa.music.ui.theme.playScreenHorizontal
import studio.mandysa.music.ui.theme.playScreenMaxWidth
import java.io.File

/**
 * @author 黄浩
 */

@Composable
fun LyricScreen() {
    var lyric by rememberSaveable { mutableStateOf("") }
    val song by PlayManager.changeMusicLiveData().observeAsState()
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
    val liveTime by PlayManager.playingMusicProgressLiveData().observeAsState(0)
    if (lyric.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = studio.mandysa.music.R.string.not_lyric),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        Lyric(
            modifier = Modifier
                .widthIn(max = playScreenMaxWidth)
                .fillMaxSize()
                .padding(horizontal = playScreenHorizontal - horizontal / 2),
            lyric = lyric,
            liveTime = liveTime
        ) {
            PlayManager.seekTo(it)
        }
    }
}