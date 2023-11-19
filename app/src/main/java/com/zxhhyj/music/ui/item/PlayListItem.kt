package com.zxhhyj.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.PlayListBean
import com.zxhhyj.music.logic.utils.MediaLibHelper
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme

@Composable
fun PlayListItem(
    playListBean: PlayListBean,
    actions: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    val songs = playListBean.songs.mapNotNull { playListSongBean ->
        MediaLibHelper.songs.find {
            it.songName == playListSongBean.songName && it.artist.name == playListSongBean.artistName && it.album.name == playListSongBean.albumName
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppAsyncImage(
            modifier = Modifier
                .padding(horizontal = horizontal, vertical = vertical)
                .size(50.dp),
            data = songs.firstOrNull()?.coverUrl
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .padding(vertical = vertical),
        ) {
            Text(
                text = playListBean.name,
                color = LocalColorScheme.current.text,
                fontSize = 15.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = stringResource(id = R.string.total_n_songs, playListBean.songs.size),
                color = LocalColorScheme.current.subText,
                fontSize = 13.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
        CompositionLocalProvider(LocalContentColor provides LocalColorScheme.current.subText) {
            actions.invoke()
        }
        Spacer(modifier = Modifier.padding(end = horizontal))
    }
}