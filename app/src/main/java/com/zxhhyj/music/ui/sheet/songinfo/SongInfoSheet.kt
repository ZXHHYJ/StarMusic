package com.zxhhyj.music.ui.sheet.songinfo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.logic.ktx.toTime
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.ui.common.image.AppAsyncImage
import com.zxhhyj.music.ui.common.card.AppCard
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.textColor
import com.zxhhyj.music.ui.theme.textColorLight
import com.zxhhyj.music.ui.theme.vertical


@Composable
fun SongInfoSheet(
    song: SongBean,
) {
    LazyColumn {
        item {
            Box(
                modifier = Modifier.padding(
                    horizontal = horizontal,
                    vertical = vertical
                )
            ) {
                Row(
                    modifier = Modifier
                        .height(70.dp)
                ) {
                    AppCard(backgroundColor = Color.Transparent, modifier = Modifier.size(70.dp)) {
                        AppAsyncImage(modifier = Modifier.fillMaxSize(), url = song.album.coverUrl)
                    }
                    Column(modifier = Modifier.padding(vertical)) {
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
        item {
            SongInfoItem(
                title = stringResource(id = com.zxhhyj.music.R.string.duration),
                info = song.duration.toTime()
            )
        }
        item {
            SongInfoItem(
                title = stringResource(id = com.zxhhyj.music.R.string.album),
                info = song.album.name
            )
        }
        item {
            SongInfoItem(
                title = stringResource(id = com.zxhhyj.music.R.string.bit_rate),
                info = ""
            )
        }
        item {
            val hz = song.size / (song.duration / 1000)
            SongInfoItem(
                title = stringResource(id = com.zxhhyj.music.R.string.sample_rate),
                info = "${getClosestValue(hz.toInt())} Hz"
            )
        }
        item {
            Spacer(modifier = Modifier.height(vertical))
        }
        item {
            SongInfoItem(
                title = stringResource(id = com.zxhhyj.music.R.string.file_path),
                info = song.data
            )
        }
        item {
            SongInfoItem(
                title = stringResource(id = com.zxhhyj.music.R.string.file_size),
                info = "${song.size / 1024 / 1024} MB"
            )
        }
    }
}

private fun getClosestValue(targetNum: Int): Int {
    val commonSampleRates = listOf(8000, 11025, 22050, 32000, 44100, 47250, 48000)
    var left = 0
    var right: Int
    right = commonSampleRates.size - 1
    while (left != right) {
        val midIndex = (right + left) / 2
        val mid = right - left
        val midValue = commonSampleRates[midIndex]
        if (targetNum == midValue) {
            return midIndex
        }
        if (targetNum > midValue) {
            left = midIndex
        } else {
            right = midIndex
        }
        if (mid <= 2) {
            break
        }
    }
    return if ((commonSampleRates[right] - commonSampleRates[left]) / 2 > targetNum) commonSampleRates[right] else commonSampleRates[left]
}