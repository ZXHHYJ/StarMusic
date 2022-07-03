package studio.mandysa.music.ui.theme

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import studio.mandysa.music.MainActivity

val navHeight = 56.dp

val verticalMargin = 10.dp

val horizontalMargin = 16.dp

val round = 8.dp

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
val windowSizeClass
    @Composable get() = calculateWindowSizeClass(LocalContext.current as MainActivity)

val isMedium: Boolean
    @Composable get() = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
