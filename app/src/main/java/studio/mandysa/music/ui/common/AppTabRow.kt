package studio.mandysa.music.ui.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.tabSelectColor
import studio.mandysa.music.ui.theme.tabUnSelectColor
import studio.mandysa.music.ui.theme.textColor

@Composable
fun AppTabRow(modifier: Modifier = Modifier, selectedTabIndex: Int, tabs: @Composable () -> Unit) {
    Box {
        AppDivider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
        ScrollableTabRow(
            modifier = modifier, edgePadding = horizontalMargin,
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = textColor,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .clip(shape = RoundedCornerShape(3.dp, 3.dp, 0.dp, 0.dp))
                )
            },
            divider = {
            },
        ) {
            tabs.invoke()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppTabRow(modifier: Modifier = Modifier, pagerState: PagerState, tabs: @Composable () -> Unit) {
    Box {
        AppDivider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
        ScrollableTabRow(
            modifier = modifier, edgePadding = horizontalMargin,
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            contentColor = textColor,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .pagerTabIndicatorOffset(pagerState, tabPositions)
                        .clip(shape = RoundedCornerShape(3.dp, 3.dp, 0.dp, 0.dp)),
                    color = tabSelectColor
                )
            },
            divider = {
            },
        ) {
            tabs.invoke()
        }
    }
}

@Composable
fun AppTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    selectedContentColor: Color = tabSelectColor,
    unselectedContentColor: Color = tabUnSelectColor,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Tab(
        selected,
        onClick,
        modifier,
        enabled,
        text,
        icon,
        selectedContentColor,
        unselectedContentColor,
        interactionSource
    )
}

@ExperimentalPagerApi
private fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    pageIndexMapping: (Int) -> Int = { it },
): Modifier = layout { measurable, constraints ->
    if (tabPositions.isEmpty()) {
        // If there are no pages, nothing to show
        layout(constraints.maxWidth, 0) {}
    } else {
        val currentPage = minOf(tabPositions.lastIndex, pageIndexMapping(pagerState.currentPage))
        val currentTab = tabPositions[currentPage]
        val previousTab = tabPositions.getOrNull(currentPage - 1)
        val nextTab = tabPositions.getOrNull(currentPage + 1)
        val fraction = pagerState.currentPageOffset
        val indicatorWidth = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.width, nextTab.width, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.width, previousTab.width, -fraction).roundToPx()
        } else {
            currentTab.width.roundToPx()
        }
        val indicatorOffset = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.left, nextTab.left, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.left, previousTab.left, -fraction).roundToPx()
        } else {
            currentTab.left.roundToPx()
        }
        val placeable = measurable.measure(
            Constraints(
                minWidth = indicatorWidth,
                maxWidth = indicatorWidth,
                minHeight = 0,
                maxHeight = constraints.maxHeight
            )
        )
        layout(constraints.maxWidth, maxOf(placeable.height, constraints.minHeight)) {
            placeable.placeRelative(
                indicatorOffset,
                maxOf(constraints.minHeight - placeable.height, 0)
            )
        }
    }
}
