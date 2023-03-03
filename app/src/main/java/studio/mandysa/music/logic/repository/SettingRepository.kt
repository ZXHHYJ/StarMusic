package studio.mandysa.music.logic.repository

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

object SettingRepository {
    /*大屏幕模式/平板模式*/
    val isTabletMode
        @Composable get() = LocalContext.current.resources.displayMetrics.widthPixels >= LocalContext.current.resources.displayMetrics.heightPixels
}