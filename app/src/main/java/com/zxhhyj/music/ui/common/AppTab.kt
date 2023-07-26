package com.zxhhyj.music.ui.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zxhhyj.music.ui.theme.appAccentColor
import com.zxhhyj.music.ui.theme.appBackgroundColor
import com.zxhhyj.music.ui.theme.textColorLight

@Composable
fun AppTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    indicator: @Composable
        (tabPositions: List<TabPosition>) -> Unit = @Composable { tabPositions ->
        TabRowDefaults.Indicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
            color = appAccentColor,
            height = 2.dp
        )
    },
    divider: @Composable () -> Unit = {},
    tabs: @Composable () -> Unit
) {
    TabRow(selectedTabIndex, modifier, appBackgroundColor, appAccentColor, indicator, divider, tabs)
}

@Composable
fun AppTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit
) {
    Tab(
        selected,
        onClick,
        modifier,
        enabled,
        interactionSource,
        appAccentColor,
        textColorLight,
        content
    )
}