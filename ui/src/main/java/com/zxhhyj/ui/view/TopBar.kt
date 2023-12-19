package com.zxhhyj.ui.view

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
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.StarDimens
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.math.abs
import kotlin.math.roundToInt

val toolbarHeight = 56.dp

@Stable
@Parcelize
class TopBarState : Parcelable {

    /**
     * bar大小
     */
    @IgnoredOnParcel
    var barSize by mutableStateOf(IntSize.Zero)

    /**
     * bar当前偏移高度（单位Px）
     */
    @IgnoredOnParcel
    var barOffsetHeightPx by mutableFloatStateOf(0f)

    val connection
        @Composable get() = object : NestedScrollConnection {

            /**
             * 最大偏移Px
             */
            val maxOffsetPx = barSize.height.toFloat()

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val newOffset = barOffsetHeightPx + available.y
                barOffsetHeightPx = newOffset.coerceIn(barOffsetHeightPx, 0f)
                return when {
                    newOffset < barOffsetHeightPx -> {
                        available
                    }

                    newOffset > -maxOffsetPx -> {
                        available
                    }

                    else -> {
                        available
                    }
                }
            }

            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val newOffset = barOffsetHeightPx + available.y
                return if (available.y > 0) {
                    Offset.Zero
                } else {
                    barOffsetHeightPx = newOffset.coerceIn(-maxOffsetPx, barOffsetHeightPx)
                    when {
                        newOffset < barOffsetHeightPx -> {
                            Offset.Zero
                        }

                        newOffset > -maxOffsetPx -> {
                            available
                        }

                        else -> {
                            Offset.Zero
                        }
                    }
                }
            }
        }
}

fun Modifier.bindTopBarState() = composed {
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
fun AppCenterTopBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    actions: (@Composable () -> Unit)? = null
) {
    val topBarState = LocalTopBarState.current
    Box(modifier = modifier
        .fillMaxWidth()
        .onSizeChanged {
            topBarState.barSize = it
        }
        .clickable(enabled = false) {
            //用户修复点击事件穿透的问题
        }
        .background(LocalColorScheme.current.background)
        .padding(horizontal = StarDimens.horizontal)
    ) {
        Box(
            modifier = Modifier
                .height(toolbarHeight)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
            content = {
                CompositionLocalProvider(
                    LocalTextStyle provides TextStyle(
                        color = LocalColorScheme.current.text,
                        fontSize = 18.sp
                    )
                ) {
                    title()
                }
            }
        )
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(StarDimens.horizontal / 2)
        ) {
            actions?.let {
                CompositionLocalProvider(LocalContentColor provides LocalColorScheme.current.highlight) {
                    it.invoke()
                }
            }
        }
    }
}

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    topBarProperties: TopBarProperties = TopBarProperties(),
    title: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = StarDimens.horizontal)
                .height(toolbarHeight)
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides TextStyle(
                    color = LocalColorScheme.current.text,
                    fontSize = 26.sp,
                )
            ) {
                title()
            }
        }
    }
) {
    val topBarState = LocalTopBarState.current
    val elementAlpha = (abs(topBarState.barOffsetHeightPx) / topBarState.barSize.height)
    var contentSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    topBarState.barSize = it
                }) {
            Box(
                modifier = Modifier
                    .clickable(enabled = false) {
                        //用户修复点击事件穿透的问题
                    }
                    .height(toolbarHeight)
                    .fillMaxWidth()
                    .clipToBounds()
                    .background(LocalColorScheme.current.background)
                    .padding(horizontal = StarDimens.horizontal),
                contentAlignment = Alignment.CenterEnd,
                content = {
                    Box(
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
                            .alpha(elementAlpha),
                        contentAlignment = Alignment.Center) {
                        CompositionLocalProvider(
                            LocalTextStyle provides TextStyle(
                                color = LocalColorScheme.current.text,
                                fontSize = 18.sp,
                            )
                        ) {
                            title()
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(StarDimens.horizontal / 2)) {
                        CompositionLocalProvider(LocalContentColor provides LocalColorScheme.current.highlight) {
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
                .onSizeChanged {
                    contentSize = it
                }
                .offset {
                    return@offset IntOffset(
                        x = 0,
                        y = topBarState.barOffsetHeightPx.roundToInt()
                    )
                }
                .clickable(enabled = false) {
                    //用户修复点击事件穿透的问题
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
                    .padding(horizontal = StarDimens.horizontal)
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

class TopBarProperties(
    val showCenterDivider: Boolean = true,
    val showBottomDivider: Boolean = false
)