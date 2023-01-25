package studio.mandysa.music.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

val defaultVertical = 10.dp

val defaultHorizontal = 16.dp

val defaultRound = 8.dp

val isAndroidPad: Boolean
    @Composable get() = LocalConfiguration.current.screenWidthDp.dp >= 600.dp
