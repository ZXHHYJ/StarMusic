package com.zxhhyj.music.ui.common

import android.os.Parcelable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
enum class PanelState : Parcelable { COLLAPSED, EXPANDED }

@Composable
fun rememberPanelController(panelState: PanelState): PanelController {
    val rememberPanelState = rememberSaveable {
        mutableStateOf(panelState)
    }
    return PanelController(rememberPanelState)
}

@Stable
class PanelController(panelStateMutableState: MutableState<PanelState>) {
    var panelState by panelStateMutableState
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
    panelAnimationSpec: AnimationSpec<Float> = SpringSpec(),
    content: @Composable () -> Unit,
    panel: @Composable () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        val swipeableState = rememberSwipeableState(panelController.panelState, panelAnimationSpec)
        val sizePx = with(LocalDensity.current) { (maxHeight - panelHeight).toPx() }
        val anchors = mapOf(0f to PanelState.EXPANDED, sizePx to PanelState.COLLAPSED)
        LaunchedEffect(panelController.panelState) {
            swipeableState.animateTo(panelController.panelState)
        }
        LaunchedEffect(swipeableState.currentValue) {
            panelController.swipeTo(swipeableState.currentValue)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = panelHeight)
        ) {
            content()
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
                panel()
            }
        }
    }
}