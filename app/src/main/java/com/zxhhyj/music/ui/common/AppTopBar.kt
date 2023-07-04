package com.zxhhyj.music.ui.common

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import kotlin.math.roundToInt

val toolbarHeight = 56.dp

@Stable
@Parcelize
class AppTopBarState : Parcelable {

    @IgnoredOnParcel
    var barSize by mutableStateOf(IntSize.Zero)

    @IgnoredOnParcel
    var barOffsetHeightPx by mutableStateOf(0f)

    val connection
        @Composable get() = object : NestedScrollConnection {

            val maxUpPx = barSize.height.toFloat()

            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val newOffset = barOffsetHeightPx + available.y
                barOffsetHeightPx = newOffset.coerceIn(-maxUpPx, 0f)
                if (newOffset >= -maxUpPx && newOffset <= 0f) {
                    return available
                }
                return Offset.Zero
            }
        }
}

@Composable
fun rememberAppTopBarState() = rememberSaveable { AppTopBarState() }

fun Modifier.bindAppTopBarState(state: AppTopBarState) = composed {
    nestedScroll(connection = state.connection)
        .offset {
            return@offset IntOffset(
                x = 0,
                y = state.barSize.height + state.barOffsetHeightPx.roundToInt()
            )
        }
}

@Composable
fun AppTopBar(
    state: AppTopBarState,
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
    var contentSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    state.barSize = it
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
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset {
                                return@offset IntOffset(
                                    x = 0,
                                    y = (contentSize.height + state.barOffsetHeightPx)
                                        .roundToInt()
                                        .coerceIn(0, contentSize.height)
                                )
                            }
                    )
                    CompositionLocalProvider(LocalContentColor provides appIconAccentColor) {
                        actions.invoke()
                    }
                }
            )
            Box(modifier = Modifier
                .clipToBounds()
                .padding(horizontal = horizontal)
                .onSizeChanged {
                    contentSize = it
                }
                .offset {
                    return@offset IntOffset(
                        x = 0,
                        y = state.barOffsetHeightPx.roundToInt()
                    )
                }
            ) {
                content.invoke()
            }
        }
        AppDivider(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
                .padding(horizontal = if (state.barOffsetHeightPx < 0f) 0.dp else horizontal)
                .offset {
                    return@offset IntOffset(
                        x = 0,
                        y = state.barOffsetHeightPx
                            .roundToInt()
                            .coerceIn(-contentSize.height, 0)
                    )
                })
    }
}