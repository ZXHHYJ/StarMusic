package com.zxhhyj.music.ui.screen.play

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.common.SoundQualityIcon
import com.zxhhyj.music.ui.common.WebDavIcon
import com.zxhhyj.music.ui.theme.round
import com.zxhhyj.music.ui.theme.translucentWhiteColor
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.view.AppCard
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.item.Item

@Composable
fun ColumnScope.PlayQueueScreen() {
    val playlist by PlayerManager.playListLiveData().observeAsState()
    val song by PlayerManager.currentSongLiveData().observeAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = PlayScreen.PlayScreenContentHorizontal,
                vertical = vertical
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.play_list),
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${(playlist?.indexOf(song) ?: 0) + 1}/${playlist?.size}",
                color = translucentWhiteColor,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.weight(1.0f))
        BottomNavigation(
            modifier = Modifier
                .widthIn(max = 120.dp)
                .clip(RoundedCornerShape(round)),
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        ) {
            val playMode by PlayerManager.playModeLiveData().observeAsState()
            PlayerManager.PlayMode.values().forEach {
                BottomNavigationItem(
                    selected = it == playMode,
                    onClick = { PlayerManager.setPlayMode(it) },
                    icon = {
                        when (it) {
                            PlayerManager.PlayMode.SINGLE_LOOP -> {
                                Icon(
                                    imageVector = Icons.Rounded.RepeatOne,
                                    contentDescription = null
                                )
                            }

                            PlayerManager.PlayMode.LIST_LOOP -> {
                                Icon(
                                    imageVector = Icons.Rounded.Repeat,
                                    contentDescription = null
                                )
                            }

                            PlayerManager.PlayMode.RANDOM -> {
                                Icon(
                                    imageVector = Icons.Rounded.Shuffle,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    selectedContentColor = Color.White,
                    unselectedContentColor = translucentWhiteColor,
                    modifier = Modifier.clip(RoundedCornerShape(0))
                )
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1.0f)
    ) {

        val lazyListState = rememberLazyListState()

        var selectItemBoxSize by remember {
            mutableStateOf(IntSize.Zero)
        }

        val currentSong by PlayerManager.currentSongLiveData().observeAsState()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            playlist?.let { songBeans ->
                itemsIndexed(songBeans) { index, model ->
                    AppCard(
                        backgroundColor = if (currentSong == model) Color.White.copy(alpha = 0.2f) else Color.Transparent,
                        modifier = Modifier
                            .onSizeChanged {
                                selectItemBoxSize = it
                            }
                            .fillMaxWidth()
                    ) {
                        Item(
                            icon = {
                                AppAsyncImage(
                                    modifier = Modifier
                                        .size(50.dp),
                                    data = model.coverUrl
                                )
                            },
                            text = {
                                Text(
                                    text = model.songName,
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            subText = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    if (SettingRepository.EnableShowSoundQualityLabel) {
                                        SoundQualityIcon(song = model)
                                    }
                                    if (model is SongBean.WebDav) {
                                        WebDavIcon()
                                    }
                                    Text(
                                        text = model.artist.name,
                                        color = translucentWhiteColor,
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            },
                            actions = {
                                AppIconButton(onClick = {
                                    PlayerManager.removeSong(model)
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Remove,
                                        contentDescription = null,
                                        tint = translucentWhiteColor
                                    )
                                }
                            }) {
                            PlayerManager.play(songBeans, index)
                        }
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
        LaunchedEffect(song) {
            val position = playlist?.indexOf(song) ?: 0
            val height = (boxHeightPx - selectItemBoxSize.height) / 2
            lazyListState.animateScrollToItem(position.coerceAtLeast(0), -height)
        }
    }
}