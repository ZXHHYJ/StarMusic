package com.zxhhyj.music.ui.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.roundShape
import com.zxhhyj.music.ui.theme.translucentWhite
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit


//部分代码参考了:https://juejin.cn/post/7046235192616779784
//特别感谢:Kyant

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Lyric(
    modifier: Modifier = Modifier,
    lyric: String,
    liveTime: Int,
    onClick: (Int) -> Unit
) {
    /**
     * 处理时间
     * 时间转换为毫秒millisecond
     */
    fun timeStr(timeStr: String): Int {
        val timeData =
            timeStr
                .replace(".", ":")
                .split(":")
        val minute = timeData[0].toInt()
        val second = timeData[1].toInt()
        val millisecond = timeData[2].toInt()
        return (minute.minutes + second.seconds + millisecond.milliseconds).toInt(DurationUnit.MILLISECONDS)
    }

    val lazyListState = rememberLazyListState()

    var position by rememberSaveable {
        mutableStateOf(0)
    }

    var lyrics by rememberSaveable {
        mutableStateOf(listOf<Pair<String, Int>>())
    }

    var selectLyricItemBoxSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    BoxWithConstraints {
        LazyColumn(
            modifier = modifier
                .graphicsLayer { alpha = 0.99F }
                .drawWithContent {
                    val colors = listOf(
                        Color.Transparent, Color.Black, Color.Black, Color.Black, Color.Black,
                        Color.Black, Color.Black, Color.Black, Color.Transparent
                    )
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(colors),
                        blendMode = BlendMode.DstIn
                    )
                },
            state = lazyListState
        ) {
            item {
                Spacer(modifier = Modifier.height(maxHeight / 2))
            }
            itemsIndexed(lyrics) { index, model ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(roundShape)
                        .clickable {
                            onClick.invoke(model.second)
                        }
                        .padding(
                            vertical = 20.dp,
                            horizontal = horizontal / 2
                        )
                        .animateItemPlacement()
                        .onSizeChanged {
                            if (position == index) {
                                selectLyricItemBoxSize = it
                            }
                        },
                    text = model.first,
                    color = if (position == index) Color.White else translucentWhite,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Spacer(modifier = Modifier.height(maxHeight / 2))
            }
        }
        LaunchedEffect(lyric) {
            val list = arrayListOf<Pair<String, Int>>()
            for (string in lyric.split("\n")) {
                val data = string.replace("[", "").split("]")
                for (i in 0 until data.size - 1) {
                    try {
                        data[data.size - 1].trim().let {
                            if (it.isNotEmpty()) {
                                list.add(it to timeStr(data[i]))
                            }
                        }
                    } catch (_: Exception) {
                    }
                }
            }
            lyrics = list
        }
        LaunchedEffect(liveTime) {
            lyrics.forEachIndexed { index, lrcContent ->
                if (liveTime >= lrcContent.second) {
                    position = index
                }
            }
        }
        val lyricHeightPx = with(LocalDensity.current) { maxHeight.roundToPx() }
        LaunchedEffect(position, selectLyricItemBoxSize) {
            if (!lazyListState.isScrollInProgress) {
                val height = (lyricHeightPx - selectLyricItemBoxSize.height) / 2
                lazyListState.animateScrollToItem((position + 1).coerceAtLeast(0), -height)
            }
        }
    }
}