package studio.mandysa.music.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppDivider() {
    if (isSystemInDarkTheme()) {
        Divider(thickness = 1.dp, color = Color.Gray)
        return
    }
    Divider(thickness = 1.dp)
}