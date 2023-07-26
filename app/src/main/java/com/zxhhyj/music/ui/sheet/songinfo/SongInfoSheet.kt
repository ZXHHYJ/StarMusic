package com.zxhhyj.music.ui.sheet.songinfo

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.logic.ktx.toTime
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.ui.theme.vertical

@Composable
fun SongInfoSheet(
    song: SongBean,
) {
    LazyColumn {
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
        song.bitrate?.let {
            item {
                SongInfoItem(
                    title = stringResource(id = com.zxhhyj.music.R.string.bit_rate),
                    info = "${song.bitrate} kbps"
                )
            }
        }
        item {
            SongInfoItem(
                title = stringResource(id = com.zxhhyj.music.R.string.sample_rate),
                info = "${song.samplingRate} Hz"
            )
        }
        /*item {
            SongInfoItem(
                title = stringResource(id = com.zxhhyj.music.R.string.num_tracks),
                info = "${song.numTracks}"
            )
        }*/
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