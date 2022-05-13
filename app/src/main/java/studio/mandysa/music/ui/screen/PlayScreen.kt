package studio.mandysa.music.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.map
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.compose.MandySaMusicKenBurns

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PlayScreen() {
    Box {
        val coverUrl by PlayManager.changeMusicLiveData().map {
            it.coverUrl
        }.observeAsState("")
        val paused by PlayManager.pauseLiveData().observeAsState(true)
        MandySaMusicKenBurns(
            modifier = Modifier.fillMaxSize(),
            imageUrl = coverUrl,
            paused = paused
        )
        HorizontalPager(modifier = Modifier.fillMaxSize(), count = 2) {
            when (it) {
                0 -> {
                    CurrentPlayScreen()
                }
                1 -> {}
                2 -> {}
            }
        }
    }
}

@Composable
private fun CurrentPlayScreen() {

}