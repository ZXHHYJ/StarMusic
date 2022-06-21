package studio.mandysa.music.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class PanelState {
    COLLAPSED, EXPANDED
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SlidingPanel(
    modifier: Modifier = Modifier,
    panelHeight: Dp,
    panelStateChange: (PanelState) -> Unit,
    content: @Composable ((PanelState) -> Unit) -> Unit,
    panel: @Composable ((PanelState) -> Unit) -> Unit
) {
    //0开1关
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        val swipeableState = rememberSwipeableState(PanelState.COLLAPSED)
        val sizePx = with(LocalDensity.current) { (maxHeight - panelHeight).toPx() }
        val anchors = mapOf(0f to PanelState.EXPANDED, sizePx to PanelState.COLLAPSED)
        val scope = rememberCoroutineScope()
        val animateTo: (PanelState) -> Unit = {
            scope.launch {
                panelStateChange.invoke(it)
                swipeableState.animateTo(it)
            }
        }
        LaunchedEffect(swipeableState.currentValue) {
            panelStateChange.invoke(swipeableState.currentValue)
        }
        Box(
            modifier = Modifier
                .fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = panelHeight)
            ) {
                content.invoke(animateTo)
            }
            Box(
                Modifier
                    .absoluteOffset {
                        IntOffset(
                            0,
                            swipeableState.offset.value.roundToInt()
                        )
                    }
                    .background(Color.White)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .swipeable(
                            state = swipeableState,
                            anchors = anchors,
                            resistance = null,
                            orientation = Orientation.Vertical
                        )
                ) {
                    panel.invoke(animateTo)
                }
            }
        }
    }
}