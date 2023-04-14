package com.zxhhyj.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.LocalMediaRepository.songs
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.service.playmanager.ktx.coverUrl
import com.zxhhyj.music.ui.composable.AppAsyncImage
import com.zxhhyj.music.ui.composable.AppCard
import com.zxhhyj.music.ui.composable.AppRoundCard
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.textColor
import com.zxhhyj.music.ui.theme.textColorLight
import com.zxhhyj.music.ui.theme.vertical

@Composable
fun ArtistItem(artist: SongBean.Local.Artist, onClick: () -> Unit) {
    AppCard(backgroundColor = Color.Transparent) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppRoundCard(
                backgroundColor = Color.Transparent,
                modifier = Modifier
                    .padding(horizontal = horizontal, vertical = vertical)
                    .size(50.dp)
            ) {
                AppAsyncImage(modifier = Modifier.fillMaxSize(), url = artist.coverUrl)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
                    .padding(vertical = vertical),
            ) {
                Text(
                    text = artist.name,
                    color = textColor,
                    fontSize = 15.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = stringResource(id = R.string.total_n_songs, artist.songs.size),
                    color = textColorLight,
                    fontSize = 13.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
