package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun ScreenLayout(modifier: Modifier, content: @Composable ScreenLayoutScope.() -> Unit) {
    BoxWithConstraints(modifier = modifier) {
        content.invoke(ScreenLayoutScope(this.maxWidth, this.maxHeight))
    }
}

class ScreenLayoutScope(private val width: Dp, private val height: Dp) {

    val Int.wpp: Dp
        get() = width * this / 100

    val Int.hpp: Dp
        get() = height * this / 100
}