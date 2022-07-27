package studio.mandysa.music.ui.screen.play

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.map
import com.drake.net.exception.HttpFailureException
import com.drake.net.exception.NoCacheException
import studio.mandysa.music.R
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.config.mainApplication
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.POPWindows
import studio.mandysa.music.ui.common.lyric.Lyric

@Composable
fun LyricScreen() {
    val musicId by PlayManager.changeMusicLiveData().map {
        it.id
    }.observeAsState()
    var lyric by remember { mutableStateOf("") }
    val liveTime by PlayManager.playingMusicProgressLiveData().observeAsState(0)
    LaunchedEffect(musicId) {
        musicId?.let {
            try {
                lyric = api.shortCache().getLyric(it)
            } catch (e: NoCacheException) {
                lyric = api.network().getLyric(it)
            } catch (e: HttpFailureException) {
                POPWindows.postValue(mainApplication.getString(R.string.network_error))
            } catch (e: Exception) {
            }
        }
    }
    Lyric(modifier = Modifier.fillMaxSize(), lyric = lyric, liveTime = liveTime) {
        PlayManager.seekTo(it)
    }
}