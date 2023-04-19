package com.zxhhyj.music.ui.screen.play

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.ui.common.AppCard
import com.zxhhyj.music.ui.common.AppDivider
import com.zxhhyj.music.ui.common.AppIcon
import com.zxhhyj.music.ui.theme.translucentWhite
import com.zxhhyj.music.ui.theme.vertical

@Composable
fun ColumnScope.PlayQueueScreen() {
    val playlist by PlayManager.changePlayListLiveData().observeAsState()
    val song by PlayManager.changeMusicLiveData().observeAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PlayScreen.PlayScreenContentHorizontal, vertical = vertical)
    ) {
        Text(
            text = "${(playlist?.indexOf(song) ?: 0) + 1}/${playlist?.size}",
            color = translucentWhite,
            fontSize = 14.sp
        )
        Text(
            text = stringResource(id = R.string.play_list),
            modifier = Modifier.weight(1.0f),
            color = Color.White,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.clear),
            color = translucentWhite,
            fontSize = 14.sp,
            modifier = Modifier.clickable {
                PlayManager.clearPlayList()
            })
    }

    AppDivider(
        color = translucentWhite,
        modifier = Modifier.padding(horizontal = PlayScreen.PlayScreenContentHorizontal)
    )

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
            modifier = Modifier.fillMaxSize(), state = lazyListState
        ) {
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
        }
        val boxHeightPx = with(LocalDensity.current) {
            maxHeight.roundToPx()
        }
        LaunchedEffect(Unit) {
            val position = playlist?.indexOf(song) ?: 0
            val height = (boxHeightPx - selectItemBoxSize.height) / 2
            lazyListState.scrollToItem(position.coerceAtLeast(0), -height)
        }
        LaunchedEffect(playlist, song) {
            val position = playlist?.indexOf(song) ?: 0
            val height = (boxHeightPx - selectItemBoxSize.height) / 2
            lazyListState.animateScrollToItem(position.coerceAtLeast(0), -height)
        }
    }
}

@Composable
private fun QueueSongItem(
    modifier: Modifier,
    song: SongBean,
    onClick: () -> Unit
) {
    AppCard(
        backgroundColor = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
                .padding(horizontal = PlayScreen.PlayScreenContentHorizontal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
                    .padding(vertical = vertical),
            ) {
                Text(
                    text = song.songName,
                    color = Color.White,
                    fontSize = 15.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = song.artist.name,
                    color = translucentWhite,
                    fontSize = 13.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow =
                    TextOverflow.Ellipsis
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
        }
    }
}