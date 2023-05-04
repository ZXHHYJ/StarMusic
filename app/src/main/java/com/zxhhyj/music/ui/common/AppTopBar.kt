package com.zxhhyj.music.ui.common

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
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
import kotlin.math.roundToInt

val toolbarHeight = 56.dp

@Stable
@Parcelize
class TopAppBarState : Parcelable {

    @IgnoredOnParcel
    var topAppBarHeight by mutableStateOf(IntSize.Zero)

    @IgnoredOnParcel
    var topAppBarOffsetHeightPx by mutableStateOf(0f)

    val connection
        @Composable get() = object : NestedScrollConnection {

            val maxUpPx = topAppBarHeight.height.toFloat()

            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val newOffset = topAppBarOffsetHeightPx + available.y
                topAppBarOffsetHeightPx = newOffset.coerceIn(-maxUpPx, 0f)
                if (newOffset >= -maxUpPx && newOffset <= 0f) {
                    return available
                }
                return Offset.Zero
            }
        }
}

@Composable
fun rememberTopAppBarState() = rememberSaveable { TopAppBarState() }

fun Modifier.bindTopAppBarState(state: TopAppBarState) = composed {
    nestedScroll(connection = state.connection)
        .offset {
            return@offset IntOffset(
                x = 0,
                y = state.topAppBarHeight.height + state.topAppBarOffsetHeightPx.roundToInt()
            )
        }
}

@Composable
fun TopAppBar(
    state: TopAppBarState,
    modifier: Modifier,
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    val topAppBarHeightPx =
        with(LocalDensity.current) { toolbarHeight.toPx() }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    state.topAppBarHeight = it
                }) {
            Row(
                modifier = Modifier
                    .height(toolbarHeight)
                    .clipToBounds()
                    .background(appBackgroundColor)
                    .padding(horizontal = horizontal),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1.0f)
                            .offset {
                                return@offset IntOffset(
                                    x = 0,
                                    y = (topAppBarHeightPx + state.topAppBarOffsetHeightPx)
                                        .coerceIn(0f, topAppBarHeightPx)
                                        .roundToInt()
                                )
                            }
                    )
                    CompositionLocalProvider(LocalContentColor provides appIconAccentColor) {
                        actions.invoke(this)
                    }
                }
            )
            Column(modifier = Modifier
                .height(toolbarHeight)
                .clipToBounds()
                .padding(horizontal = horizontal)
                .offset {
                    return@offset IntOffset(
                        x = 0,
                        y = state.topAppBarOffsetHeightPx.roundToInt()
                    )
                }) {
                Text(
                    text = title,
                    color = textColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f)
                )
            }
        }
        AppDivider(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
                .padding(horizontal = horizontal)
                .offset {
                    return@offset IntOffset(
                        x = 0,
                        y = state.topAppBarOffsetHeightPx
                            .coerceIn(-topAppBarHeightPx, 0f)
                            .roundToInt()
                    )
                })
    }

}