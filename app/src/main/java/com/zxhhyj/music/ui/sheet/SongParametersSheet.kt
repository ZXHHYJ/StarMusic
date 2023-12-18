package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.utils.CopyUtils
import com.zxhhyj.music.logic.utils.timestampToString
import com.zxhhyj.music.logic.utils.toTimeString
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer

@Composable
fun SongParametersSheet(
    song: SongBean,
) {
    LazyColumn {
        item {
            ItemSpacer()
        }
        item {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                song.duration?.let {
                    SongParameterItem(
                        title = stringResource(id = R.string.song_duration),
                        info = it.toTimeString()
                    )
                    ItemDivider()
                }
                SongParameterItem(
                    title = stringResource(id = R.string.album),
                    info = song.albumName ?: stringResource(id = R.string.unknown_album)
                )
                ItemDivider()
                song.bitrate?.let {
                    SongParameterItem(
                        title = stringResource(id = R.string.bit_rate),
                        info = "$it kbps"
                    )
                    ItemDivider()
                }
                song.samplingRate?.let {
                    SongParameterItem(
                        title = stringResource(id = R.string.sample_rate),
                        info = "$it Hz"
                    )
                    ItemDivider()
                }
                SongParameterItem(
                    title = stringResource(id = R.string.file_path),
                    info = song.data
                )
                ItemDivider()
                SongParameterItem(
                    title = stringResource(id = R.string.file_size),
                    info = "${song.size / 1024 / 1024} MB"
                )
                ItemDivider()
                SongParameterItem(
                    title = stringResource(id = R.string.date_modified),
                    info = timestampToString(song.dateModified)
                )
            }
        }
    }
}

@Composable
private fun SongParameterItem(title: String, info: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(horizontal = horizontal, vertical = vertical)) {
        Text(
            text = title,
            color = LocalColorScheme.current.highlight,
            style = LocalTextStyles.current.main
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Text(
            text = info,
            color = LocalColorScheme.current.subText,
            style = LocalTextStyles.current.main,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .padding(start = horizontal)
                .clickable {
                    CopyUtils.copyText(info)
                })
    }
}