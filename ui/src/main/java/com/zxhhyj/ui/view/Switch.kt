package com.zxhhyj.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.zxhhyj.ui.theme.LocalColorScheme
import kotlinx.coroutines.launch

enum class Status {
    CLOSE, OPEN
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Switch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
) {
    BoxWithConstraints(modifier = modifier) {
        val blockSize = min(maxHeight, maxWidth)
        val blockSizePx = with(LocalDensity.current) { blockSize.toPx() }
        val swipeableState =
            rememberSwipeableState(initialValue = if (checked) Status.OPEN else Status.CLOSE)
        LaunchedEffect(checked) {
            swipeableState.animateTo(if (checked) Status.OPEN else Status.CLOSE)
        }
        var openProgress by rememberSaveable {
            mutableStateOf(0f)
        }
        LaunchedEffect(swipeableState.progress) {
            swipeableState.progress.run {
                if (from == Status.OPEN && to == Status.OPEN) {
                    openProgress = (fraction)
                } else
                    if (from == Status.OPEN && to == Status.CLOSE) {
                        openProgress = (1f - fraction)
                    } else
                        if (from == Status.CLOSE && to == Status.CLOSE) {
                            openProgress = (1f - fraction)
                        } else
                            if (from == Status.CLOSE && to == Status.OPEN) {
                                openProgress = (fraction)
                            }
            }
        }
        LaunchedEffect(swipeableState.currentValue) {
            if (swipeableState.currentValue == Status.OPEN) {
                onCheckedChange.invoke(true)
            } else {
                onCheckedChange.invoke(false)
            }
        }
        Card(
            shape = RoundedCornerShape(50),
            backgroundColor = Color.LightGray,
            border = BorderStroke(
                1.dp,
                LocalColorScheme.current.highlight.copy(alpha = openProgress)
            ),
            elevation = 0.dp
        ) {
            val coroutineScope = rememberCoroutineScope()
            Box(
                modifier = Modifier
                    .size(height = blockSize, width = blockSize * 2)
                    .background(color = LocalColorScheme.current.highlight.copy(alpha = openProgress))
                    .clickable {
                        coroutineScope.launch {
                            if (!checked) {
                                swipeableState.animateTo(Status.OPEN)
                            } else {
                                swipeableState.animateTo(Status.CLOSE)
                            }
                        }
                    }, contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(swipeableState.offset.value.toInt(), 0)
                        }
                        .swipeable(
                            state = swipeableState,
                            anchors = mapOf(
                                0f to Status.CLOSE,
                                blockSizePx to Status.OPEN
                            ),
                            resistance = null,
                            orientation = Orientation.Horizontal
                        )
                        .size(blockSize)
                        .padding(2.dp)
                        .background(shape = RoundedCornerShape(50), color = Color.White)
                )
            }
        }
    }
}