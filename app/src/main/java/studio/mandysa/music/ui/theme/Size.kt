package studio.mandysa.music.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

val verticalMargin = 10.dp

val horizontalMargin = 16.dp

val round = 8.dp

val isAndroidPad: Boolean
    @Composable get() = LocalConfiguration.current.screenWidthDp.dp >= 600.dp
