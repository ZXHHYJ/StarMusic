package studio.mandysa.music.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.dirror.lyricviewx.LyricViewX

@Composable
fun Lyric(modifier: Modifier = Modifier) {
    AndroidView(factory = {
        LyricViewX(it).apply {

        }
    }) {

    }
}