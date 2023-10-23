package com.zxhhyj.music.ui.common.lyric

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import com.zxhhyj.music.R
import com.zxhhyj.music.ui.common.lyric.SyncedLyrics.Companion.toSyncedLyrics
import com.zxhhyj.music.ui.theme.round
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * 部分代码参考了:https://juejin.cn/post/7046235192616779784
 *特别感谢:Kyant
 */
@Composable
fun Lyric(
    modifier: Modifier = Modifier,
    lyric: String?,
    liveTime: Int,
    translation: Boolean,
    lyricItem: @Composable (modifier: Modifier, lyric: String, index: Int, position: Int) -> Unit,
    lyricScrollAnimationSpec: AnimationSpec<Float> = tween(1000),
    onClick: (Int) -> Unit
) {
    BoxWithConstraints(modifier) {
        lyric ?: run {
            lyricItem.invoke(
                Modifier.align(Alignment.Center),
                stringResource(id = R.string.no_lyrics),
                0,
                0
            )
            return@BoxWithConstraints
        }

        val lazyListState = rememberLazyListState()

        val coroutineScope = rememberCoroutineScope()

        val lyricList by remember(lyric, translation) {
            mutableStateOf(
                if (translation) {
                    lyric.split("\n").toSyncedLyrics().all
                } else {
                    lyric.split("\n").toSyncedLyrics().main
                }
            )
        }

        var position by remember {
            mutableIntStateOf(lyricList.indexOfLast { liveTime >= it.first }.coerceAtLeast(0))
        }

        var selectLyricItemSize by remember {
            mutableStateOf(IntSize.Zero)
        }

        var enableLyricScroll by remember {
            mutableStateOf(true)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
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
                lyricItem(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(round))
                        .clickable {
                            enableLyricScroll = true
                            onClick(model.first.toInt())
                        }
                        .onSizeChanged {
                            if (position == index) {
                                selectLyricItemSize = it
                            }
                        },
                    model.second,
                    index,
                    position
                )
            }
            item {
                Spacer(modifier = Modifier.height(maxHeight / 2))
            }
        }

        val lyricHeightPx = with(LocalDensity.current) { maxHeight.roundToPx() }
        LaunchedEffect(Unit) {
            lyricList.forEachIndexed { index, lrcContent ->
                if (liveTime >= lrcContent.first) {
                    val offset = (lyricHeightPx - selectLyricItemSize.height) / 2
                    lazyListState.scrollToItem((index + 1).coerceAtLeast(0), -offset)
                    return@forEachIndexed
                }
            }
        }
        LaunchedEffect(position) {
            if (enableLyricScroll) {
                val offset = (lyricHeightPx - selectLyricItemSize.height) / 2
                val index = position + 1
                val layoutInfo = lazyListState.layoutInfo
                val targetItemLayoutInfo =
                    layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }

                if (targetItemLayoutInfo != null) {
                    val scrollOffset = targetItemLayoutInfo.offset - layoutInfo.viewportStartOffset
                    lazyListState.animateScrollBy(
                        scrollOffset.toFloat() - offset,
                        animationSpec = lyricScrollAnimationSpec
                    )
                } else {
                    lazyListState.animateScrollToItem(index, -offset)
                }
            }
        }
        LaunchedEffect(liveTime) {
            position = lyricList.indexOfLast { liveTime >= it.first }.coerceAtLeast(0)
        }
    }
}