package com.zxhhyj.music.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import com.zxhhyj.music.ui.theme.round
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit


/**
 * 部分代码参考了:https://juejin.cn/post/7046235192616779784
 *特别感谢:Kyant
 */
@Composable
fun Lyric(
    modifier: Modifier = Modifier,
    lyric: String,
    liveTime: Int,
    translation: Boolean,
    lyricItem: @Composable (modifier: Modifier, lyric: String, index: Int, position: Int) -> Unit,
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

    var lyricList by rememberSaveable {
        mutableStateOf(listOf<Pair<String, Int>>())
    }

    var selectLyricItemSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    var enableLyricScroll by remember {
        mutableStateOf(true)
    }

    val coroutineScope = rememberCoroutineScope()

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
                }
                .scrollable(state = rememberScrollableState {
                    if (enableLyricScroll) {
                        coroutineScope.launch {
                            enableLyricScroll = false
                            delay(1500)
                            enableLyricScroll = true
                        }
                    }
                    it
                }, orientation = Orientation.Vertical),
            state = lazyListState
        ) {
            item {
                Spacer(modifier = Modifier.height(maxHeight / 2))
            }
            itemsIndexed(lyricList) { index, model ->
                lyricItem.invoke(Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(round))
                    .clickable {
                        onClick.invoke(model.second)
                    }
                    .onSizeChanged {
                        if (position == index) {
                            selectLyricItemSize = it
                        }
                    }, model.first, index, position
                )
            }
            item {
                Spacer(modifier = Modifier.height(maxHeight / 2))
            }
        }
        LaunchedEffect(lyric) {
            val lyricMap = linkedMapOf<String, String>()
            val pattern = Regex("""\[(.*?)](.*)""")
            lyric.replace("//", "").lineSequence().forEach { line ->
                pattern.matchEntire(line)?.let { matchResult ->
                    val (timestamp, s) = matchResult.destructured
                    if (s.trim().isNotEmpty()) {
                        val hadLyric = lyricMap[timestamp] != null
                        if (hadLyric) {
                            //如果出现时间戳重复就是歌词中存在翻译
                            if (!translation) {
                                return@forEach
                            }
                            lyricMap[timestamp] = lyricMap[timestamp] + "\n" + s
                        } else {
                            lyricMap[timestamp] = s
                        }
                    }
                }
            }
            lyricList = lyricMap.map {
                it.value to timeStr(it.key)
            }
        }
        LaunchedEffect(liveTime) {
            lyricList.forEachIndexed { index, lrcContent ->
                if (liveTime >= lrcContent.second) {
                    position = index
                }
            }
        }
        val lyricHeightPx = with(LocalDensity.current) { maxHeight.roundToPx() }
        LaunchedEffect(Unit) {
            val height = (lyricHeightPx - selectLyricItemSize.height) / 2
            val index = (position + 1).coerceAtLeast(0)
            lazyListState.scrollToItem(index, -height)
        }
        LaunchedEffect(position, selectLyricItemSize) {
            if (enableLyricScroll) {
                val height = (lyricHeightPx - selectLyricItemSize.height) / 2
                val index = (position + 1).coerceAtLeast(0)
                lazyListState.animateScrollToItem(index, -height)
            }
        }
    }
}

