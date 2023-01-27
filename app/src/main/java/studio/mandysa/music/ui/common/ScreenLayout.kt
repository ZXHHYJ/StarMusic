package studio.mandysa.music.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp

@Composable
fun ScreenLayout(modifier: Modifier, content: @Composable ScreenLayoutScope.() -> Unit) {
    BoxWithConstraints(modifier = modifier) {
        content.invoke(ScreenLayoutScope(this.maxWidth, this.maxHeight))
    }
}

class ScreenLayoutScope(private val width: Dp, private val height: Dp) {

    val isTablet: Boolean
        @Composable get() = (LocalContext.current.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE

    val isPortrait: Boolean
        @Composable get() = LocalContext.current.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val screenMode
        @Composable  get() = run {
            if (isTablet && isPortrait)
                ScreenMode.TabletPort
            else if (isTablet)
                ScreenMode.TabletLand
            else if (isPortrait)
                ScreenMode.PhonePort
            else
                ScreenMode.PhoneLand
        }

    val Int.wpp: Dp
        get() = width * this / 100

    val Int.hpp: Dp
        get() = height * this / 100
}

sealed class ScreenMode {
    object PhoneLand : ScreenMode()
    object PhonePort : ScreenMode()
    object TabletLand : ScreenMode()
    object TabletPort : ScreenMode()
}

