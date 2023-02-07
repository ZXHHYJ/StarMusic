package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp

@Composable
fun ScreenLayout(modifier: Modifier, content: @Composable ScreenLayoutScope.() -> Unit) {
    BoxWithConstraints(modifier = modifier) {
        content.invoke(ScreenLayoutScope(constraints, maxHeight, maxWidth, minHeight, minWidth))
    }
}

class ScreenLayoutScope(
    val constraints: Constraints,
    val maxHeight: Dp,
    val maxWidth: Dp,
    val minHeight: Dp,
    val minWidth: Dp
) {

    val Int.wpp: Dp
        get() = maxWidth * this / 100

    val Int.hpp: Dp
        get() = maxHeight * this / 100

}

sealed class ScreenMode {
    object PhoneLand : ScreenMode()
    object PhonePort : ScreenMode()
    object TabletLand : ScreenMode()
    object TabletPort : ScreenMode()
}

