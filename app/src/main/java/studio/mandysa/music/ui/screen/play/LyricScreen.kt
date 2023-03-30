package studio.mandysa.music.ui.screen.play

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.ui.common.Lyric
import studio.mandysa.music.ui.theme.smallHorizontal
import studio.mandysa.music.ui.theme.playScreenHorizontal
import studio.mandysa.music.ui.theme.playScreenMaxWidth
import java.io.File


@Composable
fun LyricScreen() {
    var lyric by remember { mutableStateOf("") }
    val song by PlayManager.changeMusicLiveData().observeAsState()
    LaunchedEffect(song) {
        when (song) {
            is SongBean.Local -> {
                launch(Dispatchers.IO) {
                    val model = (song as SongBean.Local)
                    try {
                        val audioFile = AudioFileIO.read(File(model.data))
                        lyric = audioFile.tag.getFirst(FieldKey.LYRICS)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            is SongBean.Network -> {
                // TODO: 在线歌词
                /*val model = (song as SongBean.Network)
                try {
                    lyric = api.longCache().getLyric(model.id)
                } catch (e: NoCacheException) {
                    lyric = api.network().getLyric(model.id)
                } catch (e: HttpFailureException) {
                    lyric = ""
                } catch (_: Exception) {
                }*/
            }
            null -> {}
        }
    }
    // TODO: 补充无歌词界面
    val liveTime by PlayManager.playingMusicProgressLiveData().observeAsState(0)
    Lyric(
        modifier = Modifier
            .widthIn(max = playScreenMaxWidth)
            .fillMaxSize()
            .padding(horizontal = playScreenHorizontal - smallHorizontal / 2),
        lyric = lyric,
        liveTime = liveTime
    ) {
        PlayManager.seekTo(it)
    }
}