package studio.mandysa.music.ui.common

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import studio.mandysa.music.ui.theme.appDividerColor



@Composable
fun AppDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier,
        color = appDividerColor,
        thickness = thickness,
        startIndent = startIndent
    )
}