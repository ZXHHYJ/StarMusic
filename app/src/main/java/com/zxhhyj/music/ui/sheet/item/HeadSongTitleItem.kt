package com.zxhhyj.music.ui.sheet.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.service.playmanager.ktx.coverUrl
import com.zxhhyj.music.ui.common.AppCard
import com.zxhhyj.music.ui.common.image.AppAsyncImage
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.textColor
import com.zxhhyj.music.ui.theme.textColorLight
import com.zxhhyj.music.ui.theme.vertical

@Composable
fun HeadSongTitleItem(song: SongBean) {
    Box(
        modifier = Modifier.padding(
            horizontal = horizontal,
            vertical = vertical
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AppCard(backgroundColor = Color.Transparent, modifier = Modifier.size(60.dp)) {
                AppAsyncImage(modifier = Modifier.fillMaxSize(), url = song.album.coverUrl)
            }
            Column(modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(vertical)) {
                Text(
                    text = song.songName,
                    color = textColor,
                    fontSize = 15.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = song.artist.name,
                    color = textColorLight,
                    fontSize = 13.sp,
                    maxLines = 1,
                )
            }
        }
    }
}