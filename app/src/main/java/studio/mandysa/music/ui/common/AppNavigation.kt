package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import studio.mandysa.music.ui.theme.barItemColor
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.isMatePad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
) {
    if (isMatePad) {
        NavigationRailItem(
            selected = selected,
            onClick = onClick,
            icon = icon,
            modifier = modifier,
            label = label,
            alwaysShowLabel = true,
            colors = NavigationRailItemDefaults.colors(indicatorColor = barItemColor)
        )
    } else {
        NavigationDrawerItem(
            icon = icon,
            label = label,
            selected = selected,
            onClick = onClick,
            modifier = modifier.padding(horizontal = horizontalMargin),
            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
        )
    }
}

@Composable
fun RowScope.AppNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = icon,
        modifier = modifier,
        label = label,
        colors = NavigationBarItemDefaults.colors(indicatorColor = barItemColor),
    )
}

