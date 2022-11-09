package studio.mandysa.music.ui.screen.play

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.drake.net.exception.HttpFailureException
import com.drake.net.exception.NoCacheException
import com.mpatric.mp3agic.Mp3File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.bean.Song
import studio.mandysa.music.ui.common.lyric.Lyric

@Composable
fun LyricScreen() {
    var lyric by remember { mutableStateOf("") }
    val song by PlayManager.changeMusicLiveData().observeAsState()
    LaunchedEffect(song) {
        when (song) {
            is Song.LocalBean -> {
                launch(Dispatchers.IO) {
                    val model = (song as Song.LocalBean)
                    lyric = try {
                        val mp3File = Mp3File(model.data)
                        mp3File.id3v2Tag.lyrics
                    } catch (e: Exception) {
                        // TODO: 部分歌曲获取歌词未知错误
                        ""
                    }
                }
            }
            is Song.NetworkBean -> {
                val model = (song as Song.NetworkBean)
                try {
                    lyric = api.longCache().getLyric(model.id)
                } catch (e: NoCacheException) {
                    lyric = api.network().getLyric(model.id)
                } catch (e: HttpFailureException) {
                    lyric = ""
                } catch (_: Exception) {
                }
            }
            null -> {}
        }
    }
    // TODO: 补充无歌词界面
    val liveTime by PlayManager.playingMusicProgressLiveData().observeAsState(0)
    Lyric(modifier = Modifier.fillMaxSize(), lyric = lyric, liveTime = liveTime) {
        PlayManager.seekTo(it)
    }
}