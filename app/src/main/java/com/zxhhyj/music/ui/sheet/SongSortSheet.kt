package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Abc
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemCheckbox
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer

val SettingRepository.SongSortEnum.itemName: String
    @Composable get() = when (this) {
        SettingRepository.SongSortEnum.SONG_NAME -> stringResource(id = R.string.song_name)
        SettingRepository.SongSortEnum.SONG_DURATION -> stringResource(id = R.string.song_duration)
        SettingRepository.SongSortEnum.SINGER_NAME -> stringResource(id = R.string.singer_name)
        SettingRepository.SongSortEnum.DATE_MODIFIED -> stringResource(id = R.string.date_modified)
    }

val SettingRepository.SongSortEnum.itemIcon: ImageVector
    @Composable get() = when (this) {
        SettingRepository.SongSortEnum.SONG_NAME -> Icons.Rounded.Abc
        SettingRepository.SongSortEnum.SONG_DURATION -> Icons.Rounded.Timer
        SettingRepository.SongSortEnum.SINGER_NAME -> Icons.Rounded.Person
        SettingRepository.SongSortEnum.DATE_MODIFIED -> Icons.Rounded.AccessTime
    }

@Composable
fun SongSortSheet() {
    Column(modifier = Modifier.fillMaxWidth()) {
        RoundColumn(modifier = Modifier.fillMaxWidth()) {
            SettingRepository.SongSortEnum.values().forEachIndexed { index, type ->
                ItemCheckbox(
                    icon = {
                        Icon(
                            imageVector = type.itemIcon,
                            contentDescription = type.itemName
                        )
                    },
                    text = { Text(text = type.itemName) },
                    subText = { },
                    checked = SettingRepository.SongSort == type.ordinal,
                    onCheckedChange = {
                        SettingRepository.SongSort = type.ordinal
                    }
                )
                if (index != SettingRepository.SongSortEnum.values().size - 1) {
                    ItemDivider()
                }
            }
        }
        ItemSpacer()
        RoundColumn(modifier = Modifier.fillMaxWidth()) {
            ItemCheckbox(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowUpward,
                        contentDescription = null
                    )
                },
                text = { Text(text = stringResource(id = R.string.ascending)) },
                subText = { },
                checked = !SettingRepository.Descending,
                onCheckedChange = {
                    SettingRepository.Descending = false
                }
            )
            ItemDivider()
            ItemCheckbox(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowDownward,
                        contentDescription = null
                    )
                },
                text = { Text(text = stringResource(id = R.string.descending)) },
                subText = { },
                checked = SettingRepository.Descending,
                onCheckedChange = {
                    SettingRepository.Descending = true
                }
            )
        }
    }
}