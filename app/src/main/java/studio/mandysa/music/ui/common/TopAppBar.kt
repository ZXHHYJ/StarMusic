package studio.mandysa.music.ui.common

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import studio.mandysa.music.ui.theme.appBackgroundColor
import studio.mandysa.music.ui.theme.horizontal
import studio.mandysa.music.ui.theme.textColor
import kotlin.math.roundToInt

/**
 * @author 黄浩
 */

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

private val toolbarHeight = 56.dp

@Composable
fun TopAppBar(
    state: TopAppBarState,
    modifier: Modifier,
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    val topAppBarHeightPx =
        with(LocalDensity.current) { toolbarHeight.toPx() }

    Surface(
        modifier = modifier,
        color = Color.Transparent,
        contentColor = textColor,
        elevation = 0.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
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
                        actions.invoke(this)
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
}

