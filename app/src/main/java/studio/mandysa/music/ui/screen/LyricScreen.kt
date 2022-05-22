package studio.mandysa.music.ui.screen

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.network.ServiceCreator
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.Lyric

@Composable
fun LyricScreen() {
    val musicId by PlayManager.changeMusicLiveData().map {
        it.id
    }.observeAsState()
    var lyric by remember { mutableStateOf(if (musicId != null) "正在获取歌词" else "") }
    LaunchedEffect(musicId) {
        withContext(Dispatchers.IO) {
            musicId?.let {
                lyric = ServiceCreator.create(NeteaseCloudMusicApi::class.java).getLyric(it)
            }
        }
    }
    val liveTime by PlayManager.playingMusicProgressLiveData().observeAsState(0)
    Lyric(lyric = lyric, liveTime = liveTime) {
        PlayManager.seekTo(it)
    }
}