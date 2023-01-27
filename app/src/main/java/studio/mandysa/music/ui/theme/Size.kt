package studio.mandysa.music.ui.theme

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

val defaultVertical = 10.dp

val defaultHorizontal = 16.dp

val defaultRound = 8.dp

val tabletMode
    @Composable get() = run {
        val isTablet =
            (LocalContext.current.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
        val isPortrait =
            LocalContext.current.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        return@run if (isTablet && isPortrait)
            true
        else if (isTablet)
            true
        else !isPortrait
    }
