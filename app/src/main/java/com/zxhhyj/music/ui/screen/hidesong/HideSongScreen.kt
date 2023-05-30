package com.zxhhyj.music.ui.screen.hidesong

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.service.playmanager.ktx.coverUrl
import com.zxhhyj.music.ui.common.AppCard
import com.zxhhyj.music.ui.common.AppRoundIcon
import com.zxhhyj.music.ui.common.TopAppBar
import com.zxhhyj.music.ui.common.bindTopAppBarState
import com.zxhhyj.music.ui.common.image.AppAsyncImage
import com.zxhhyj.music.ui.common.rememberTopAppBarState
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.onBackground
import com.zxhhyj.music.ui.theme.textColor
import com.zxhhyj.music.ui.theme.textColorLight
import com.zxhhyj.music.ui.theme.vertical

@Composable
fun HiddenSongScreen(padding: PaddingValues) {
    val topAppBarState = rememberTopAppBarState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .bindTopAppBarState(topAppBarState)
    ) {
        items(AndroidMediaLibsRepository.hideSongs) {
            HideSongItem(song = it) {
                AndroidMediaLibsRepository.unHide(it)
            }
        }
    }
    TopAppBar(
        state = topAppBarState,
        modifier = Modifier,
        title = stringResource(id = R.string.hidden_songs)
    )
}

@Composable
private fun HideSongItem(
    song: SongBean,
    onClick: () -> Unit
) {
    AppCard(backgroundColor = Color.Transparent) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clickable {
                    //不需要任何反应
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppCard(
                backgroundColor = Color.Transparent,
                modifier = Modifier
                    .padding(horizontal = horizontal, vertical = vertical)
                    .size(50.dp),
            ) {
                AppAsyncImage(modifier = Modifier.fillMaxSize(), url = song.album.coverUrl)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
                    .padding(vertical = vertical),
            ) {
                Text(
                    text = song.songName,
                    color = textColor,
                    fontSize = 15.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = song.artist.name,
                    color = textColorLight,
                    fontSize = 13.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            }
            AppRoundIcon(
                imageVector = Icons.Rounded.Remove,
                tint = onBackground,
                contentDescription = null,
                modifier = Modifier.clickable(onClick = onClick)
            )
            Spacer(modifier = Modifier.padding(end = horizontal))
        }
    }
}