package com.zxhhyj.music.ui.screen.play

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.service.playmanager.ktx.allArtist
import com.zxhhyj.music.service.playmanager.ktx.artist
import com.zxhhyj.music.service.playmanager.ktx.title
import com.zxhhyj.music.ui.composable.AppCard
import com.zxhhyj.music.ui.composable.AppIcon
import com.zxhhyj.music.ui.theme.playScreenHorizontal
import com.zxhhyj.music.ui.theme.translucentWhite
import com.zxhhyj.music.ui.theme.vertical

@Composable
fun ColumnScope.PlayQueueScreen() {
    val playlist by PlayManager.changePlayListLiveData().observeAsState()
    val song by PlayManager.changeMusicLiveData().observeAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1.0f)
    ) {

        val lazyListState = rememberLazyListState()

        var selectItemBoxSize by remember {
            mutableStateOf(IntSize.Zero)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.99F }
                .drawWithContent {
                    val colors = listOf(
                        Color.Transparent,
                        Color.Black,
                        Color.Black,
                        Color.Black,
                        Color.Black,
                        Color.Black,
                        Color.Black,
                        Color.Black,
                        Color.Transparent
                    )
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(colors),
                        blendMode = BlendMode.DstIn
                    )
                }, state = lazyListState
        ) {
            item {
                Spacer(modifier = Modifier.height(maxHeight / 2))
            }
            playlist?.let { songBeans ->
                itemsIndexed(songBeans) { index, model ->
                    QueueSongItem(
                        modifier = Modifier.onSizeChanged {
                            selectItemBoxSize = it
                        },
                        song = model
                    ) {
                        PlayManager.play(songBeans, index)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(maxHeight / 2))
            }
        }
        val boxHeightPx = with(LocalDensity.current) {
            maxHeight.roundToPx()
        }
        LaunchedEffect(playlist, song) {
            val position = playlist!!.indexOf(song)
            val height = (boxHeightPx - selectItemBoxSize.height) / 2
            lazyListState.animateScrollToItem((position + 1).coerceAtLeast(0), -height)
        }
    }
}

@Composable
private fun QueueSongItem(
    modifier: Modifier,
    song: SongBean,
    onClick: () -> Unit
) {
    AppCard(backgroundColor = Color.Transparent) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
                    .padding(start = playScreenHorizontal)
                    .padding(vertical = vertical),
            ) {
                Text(
                    text = song.title,
                    color = Color.White,
                    fontSize = 15.sp, maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = song.artist.allArtist(),
                    color = translucentWhite,
                    fontSize = 13.sp, maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
            AppIcon(
                imageVector = Icons.Rounded.Remove,
                tint = translucentWhite,
                contentDescription = null,
                modifier = Modifier
                    .clickable {

                    }
            )
            Spacer(modifier = Modifier.padding(end = playScreenHorizontal))
        }
    }
}