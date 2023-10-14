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
    value: Float, // 当前值
    onValueChange: (Float) -> Unit, // 值变化回调函数
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f, // 值范围，默认为0到1
    onDragStart: () -> Unit = {}, // 拖动开始回调函数
    onDragEnd: () -> Unit = {}, // 拖动结束回调函数
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier, // 修饰符
    thumbSize: Dp = 18.dp, // 滑块大小
    thumbColor: Color = Color.White, // 滑块颜色
    activeTrackColor: Color = LocalColorScheme.current.highlight, // 活动轨道颜色
    inactiveTrackColor: Color = LocalColorScheme.current.disabled // 非活动轨道颜色
) {
    BoxWithConstraints {
        val normalizedValue =
            (value - valueRange.start) / (valueRange.endInclusive - valueRange.start)
        val thumbOffset = normalizedValue * (constraints.maxWidth - with(LocalDensity.current) {
            thumbSize.toPx()
        })

        Box(
            modifier = modifier
                .height(thumbSize)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { onDragStart.invoke() }, // 拖动开始时调用回调函数
                        onDrag = { change, _ ->
                            val newValue = (change.position.x / constraints.maxWidth) *
                                    (valueRange.endInclusive - valueRange.start) +
                                    valueRange.start
                            onValueChange(newValue.coerceIn(valueRange)) // 将新值应用于回调函数，并确保在值范围内
                        },
                        onDragEnd = { onDragEnd.invoke() } // 拖动结束时调用回调函数
                    )
                },
            contentAlignment = Alignment.CenterStart
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
                shape = RoundedCornerShape(50),
                backgroundColor = thumbColor,
                elevation = 2.dp
            ) {
                // 可以在此处添加滑块上的内容
            }
        }
    }
}