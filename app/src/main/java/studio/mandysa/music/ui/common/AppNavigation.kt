package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import studio.mandysa.music.ui.theme.barBackgroundColor
import studio.mandysa.music.ui.theme.barContentColor

@Composable
fun AppBottomNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    BottomNavigation(
        modifier = modifier,
        backgroundColor = barBackgroundColor,
        contentColor = barContentColor,
        elevation = 0.dp,
        content = content
    )
}

@Composable
fun AppNavigationRail(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = modifier, content = content)
}

@Composable
fun ColumnScope.AppNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit
) {

}
