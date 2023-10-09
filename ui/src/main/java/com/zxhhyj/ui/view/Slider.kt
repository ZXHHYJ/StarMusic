package com.zxhhyj.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.zxhhyj.ui.theme.LocalColorScheme

@Composable
fun AppSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onDragStart: () -> Unit = {},
    onDragEnd: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    thumbSize: Dp = 18.dp,
    thumbColor: Color = LocalColorScheme.current.onText,
    activeTrackColor: Color = LocalColorScheme.current.highlight,
    inactiveTrackColor: Color = LocalColorScheme.current.disabled
) {
    BoxWithConstraints {
        val thumbOffset = value * (constraints.maxWidth - with(LocalDensity.current) {
            thumbSize.toPx()
        })
        Box(
            modifier = modifier
                .height(thumbSize)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { onDragStart.invoke() },
                        onDrag = { change, _ ->
                            val newValue = change.position.x / constraints.maxWidth
                            onValueChange(newValue.coerceIn(0f..1f))
                        },
                        onDragEnd = { onDragEnd.invoke() }
                    )
                }, contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(
                        shape = RoundedCornerShape(50),
                        color = inactiveTrackColor
                    )
                    .clip(RoundedCornerShape(50))
                    .clipToBounds()
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(with(LocalDensity.current) {
                            thumbOffset
                                .toInt()
                                .toDp() + thumbSize / 2
                        })
                        .background(color = activeTrackColor)
                )
            }
            Card(
                modifier = Modifier
                    .offset {
                        IntOffset(thumbOffset.toInt(), 0)
                    }
                    .size(thumbSize),
                shape = RoundedCornerShape(50), backgroundColor = thumbColor,
                elevation = 2.dp
            ) {

            }
        }
    }
}