package studio.mandysa.music.ui.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import studio.mandysa.music.ui.theme.barItemColor

@Composable
fun AppNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    NavigationRailItem(
        selected,
        onClick,
        icon,
        modifier,
        enabled,
        label,
        alwaysShowLabel,
        NavigationRailItemDefaults.colors(indicatorColor = barItemColor),
        interactionSource
    )
}

@Composable
fun RowScope.AppNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    NavigationBarItem(
        selected,
        onClick,
        icon,
        modifier,
        enabled,
        label,
        alwaysShowLabel,
        NavigationBarItemDefaults.colors(indicatorColor = barItemColor),
        interactionSource
    )
}

