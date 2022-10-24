package studio.mandysa.music.ui.common

import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import studio.mandysa.music.ui.theme.onBackground

@Composable
fun AppTabRow(selectedTabIndex: Int, tabs: @Composable () -> Unit) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        contentColor = onBackground,
    ) {
        tabs.invoke()
    }
}