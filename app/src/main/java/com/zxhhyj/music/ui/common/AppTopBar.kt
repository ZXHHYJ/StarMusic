package com.zxhhyj.music.ui.common

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.ui.theme.appBackgroundColor
import com.zxhhyj.music.ui.theme.appIconAccentColor
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.textColor
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.math.abs
import kotlin.math.roundToInt

val toolbarHeight = 56.dp

@Stable
@Parcelize
class AppTopBarState : Parcelable {

    /**
     * bar大小
     */
    @IgnoredOnParcel
    var barSize by mutableStateOf(IntSize.Zero)

    /**
     * bar当前偏移高度（单位Px）
     */
    @IgnoredOnParcel
    var barOffsetHeightPx by mutableStateOf(0f)

    val connection
        @Composable get() = object : NestedScrollConnection {

            /**
             * 最大偏移Px
             */
            val maxOffsetPx = barSize.height.toFloat()

            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val newOffset = barOffsetHeightPx + available.y
                barOffsetHeightPx = newOffset.coerceIn(-maxOffsetPx, 0f)
                if (newOffset >= -maxOffsetPx && newOffset <= 0f) {
                    return available
                }
                return Offset.Zero
            }
        }
}

fun Modifier.bindAppTopBarState() = composed {
    val topBarState = LocalTopBarState.current
    nestedScroll(connection = topBarState.connection)
        .offset {
            return@offset IntOffset(
                x = 0,
                y = topBarState.barSize.height + topBarState.barOffsetHeightPx.roundToInt()
            )
        }
}

@Composable
fun AppTopBar(
    topBarProperties: TopBarProperties = TopBarProperties(),
    modifier: Modifier,
    title: String,
    actions: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {
        Text(
            text = title,
            color = textColor,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .height(toolbarHeight)
        )
    }
) {
    val topBarState = LocalTopBarState.current
    val elementAlpha = (abs(topBarState.barOffsetHeightPx) / topBarState.barSize.height)
    var contentSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    Box(modifier = modifier.clickable(enabled = false) {
        //用户修复点击事件穿透的问题
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    topBarState.barSize = it
                }) {
            Box(
                modifier = Modifier
                    .height(toolbarHeight)
                    .fillMaxWidth()
                    .clipToBounds()
                    .background(appBackgroundColor)
                    .padding(horizontal = horizontal),
                contentAlignment = Alignment.CenterEnd,
                content = {
                    Text(
                        text = title,
                        color = textColor,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset {
                                return@offset IntOffset(
                                    x = 0,
                                    y = (contentSize.height + topBarState.barOffsetHeightPx)
                                        .roundToInt()
                                        .coerceIn(0, contentSize.height)
                                )
                            }
                            .alpha(elementAlpha)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(horizontal / 2)) {
                        CompositionLocalProvider(LocalContentColor provides appIconAccentColor) {
                            actions.invoke()
                        }
                    }
                }
            )
            if (topBarProperties.showCenterDivider) {
                AppDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(elementAlpha)
                )
            }
            Box(modifier = Modifier
                .clipToBounds()
                .padding(horizontal = horizontal)
                .onSizeChanged {
                    contentSize = it
                }
                .offset {
                    return@offset IntOffset(
                        x = 0,
                        y = topBarState.barOffsetHeightPx.roundToInt()
                    )
                }
            ) {
                content.invoke()
            }
        }
        if (topBarProperties.showBottomDivider) {
            AppDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomCenter)
                    .padding(horizontal = horizontal)
                    .offset {
                        return@offset IntOffset(
                            x = 0,
                            y = topBarState.barOffsetHeightPx
                                .roundToInt()
                                .coerceIn(-contentSize.height, 0)
                        )
                    })
        }
    }
}

class TopBarProperties(val showCenterDivider: Boolean = true, val showBottomDivider: Boolean = true)