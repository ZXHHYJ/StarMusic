package studio.mandysa.music.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

val verticalMargin = 10.dp

val horizontalMargin = 16.dp

val defaultRound = 10.dp

val isMedium: Boolean
    @Composable get() = with(LocalConfiguration.current) { this.screenWidthDp.dp >= 600.dp }
