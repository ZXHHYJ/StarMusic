package com.zxhhyj.music.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

enum class PanelState { COLLAPSED, EXPANDED }

@Composable
fun rememberPanelController(panelState: PanelState): PanelController = remember {
    PanelController(panelState)
}

@Stable
class PanelController(panelState: PanelState) {

    var panelState by mutableStateOf(panelState)
        private set

    fun swipeTo(state: PanelState) {
        panelState = state
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SlidingPanel(
    modifier: Modifier = Modifier,
    panelHeight: Dp,
    panelController: PanelController,
    content: @Composable () -> Unit,
    panel: @Composable () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        val swipeableState = rememberSwipeableState(panelController.panelState)
        val sizePx = with(LocalDensity.current) { (maxHeight - panelHeight).toPx() }
        val anchors = mapOf(0f to PanelState.EXPANDED, sizePx to PanelState.COLLAPSED)
        LaunchedEffect(panelController.panelState) {
            swipeableState.animateTo(panelController.panelState)
        }
        LaunchedEffect(swipeableState.targetValue) {
            panelController.swipeTo(swipeableState.targetValue)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = panelHeight)
        ) {
            content.invoke()
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
                panel.invoke()
            }
        }
    }
}