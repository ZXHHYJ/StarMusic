package com.zxhhyj.music.ui.sheet.songinfo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.utils.timestampToString
import com.zxhhyj.music.logic.utils.toTimeString
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemDivider

@Composable
fun SongInfoSheet(
    song: SongBean,
) {
    LazyColumn {
        item {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                SongInfoItem(
                    title = stringResource(id = com.zxhhyj.music.R.string.song_duration),
                    info = song.duration.toTimeString()
                )
                ItemDivider()
                SongInfoItem(
                    title = stringResource(id = com.zxhhyj.music.R.string.album),
                    info = song.album.name
                )
                ItemDivider()
                song.bitrate?.let {
                    SongInfoItem(
                        title = stringResource(id = com.zxhhyj.music.R.string.bit_rate),
                        info = "${song.bitrate} kbps"
                    )
                    ItemDivider()
                }
                SongInfoItem(
                    title = stringResource(id = com.zxhhyj.music.R.string.sample_rate),
                    info = "${song.samplingRate} Hz"
                )
                ItemDivider()
                SongInfoItem(
                    title = stringResource(id = com.zxhhyj.music.R.string.file_path),
                    info = song.data
                )
                ItemDivider()
                SongInfoItem(
                    title = stringResource(id = com.zxhhyj.music.R.string.file_size),
                    info = "${song.size / 1024 / 1024} MB"
                )
                ItemDivider()
                SongInfoItem(
                    title = stringResource(id = R.string.date_modified),
                    info = timestampToString(song.dateModified)
                )
            }
        }
    }
}