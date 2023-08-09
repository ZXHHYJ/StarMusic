package com.zxhhyj.music.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository.songs
import com.zxhhyj.music.logic.utils.coverUrl
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.common.BoxWithPercentages
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.Card

@Composable
fun AlbumItem(
    modifier: Modifier = Modifier,
    album: SongBean.Album,
    onClick: () -> Unit
) {
    Card(backgroundColor = Color.Transparent) {
        BoxWithPercentages(modifier = modifier) {
            Column(modifier = Modifier.fillMaxSize()) {
                AppAsyncImage(
                    modifier = Modifier.size(maxWidth),
                    data = album.coverUrl,
                    onClick = onClick
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = album.name,
                    color = LocalColorScheme.current.text,
                    fontSize = 14.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = stringResource(id = R.string.total_n_songs, album.songs.size),
                    color = LocalColorScheme.current.subText,
                    fontSize = 11.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}