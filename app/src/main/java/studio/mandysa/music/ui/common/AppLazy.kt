package studio.mandysa.music.ui.common

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import studio.mandysa.music.ui.theme.isMedium

@Composable
fun AppLazyVerticalGrid(
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: AppLazyVerticalGridScope.() -> Unit
) {
    isMedium.let {
        LazyVerticalGrid(
            GridCells.Fixed(2),
            modifier,
            state,
            contentPadding,
            reverseLayout,
            verticalArrangement,
            horizontalArrangement,
            flingBehavior,
            userScrollEnabled,
        ) {
            content.invoke(AppLazyVerticalGridScope(this, it))
        }
    }
}

class AppLazyVerticalGridScope(
    private val lazyGridScope: LazyGridScope,
    private val isMedium: Boolean
) {
    fun adaptiveItems(
        count: Int,
        itemContent: @Composable (LazyGridItemScope.(index: Int) -> Unit)
    ) {
        for (i in 0 until count) {
            adaptiveItem {
                itemContent.invoke(this, i)
            }
        }
    }

    fun adaptiveItem(scope: @Composable LazyGridItemScope.() -> Unit) {
        lazyGridScope.item(
            span = { GridItemSpan(if (isMedium) 1 else 2) },
            content = scope
        )
    }

    fun item(scope: @Composable LazyGridItemScope.() -> Unit) {
        lazyGridScope.item(span = { GridItemSpan(2) }, content = scope)
    }
}