package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Abc
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

val SettingRepository.SongSortType.itemName: String
    @Composable get() = when (this) {
        SettingRepository.SongSortType.SONG_NAME -> stringResource(id = R.string.song_name)
        SettingRepository.SongSortType.DURATION -> stringResource(id = R.string.duration)
        SettingRepository.SongSortType.SINGER_NAME -> stringResource(id = R.string.singer_name)
    }

val SettingRepository.SongSortType.itemIcon: ImageVector
    @Composable get() = when (this) {
        SettingRepository.SongSortType.SONG_NAME -> Icons.Rounded.Abc
        SettingRepository.SongSortType.DURATION -> Icons.Rounded.Timer
        SettingRepository.SongSortType.SINGER_NAME -> Icons.Rounded.Person
    }

@Composable
fun SongSortSheet() {
    Column(modifier = Modifier.fillMaxWidth()) {
        RoundColumn(modifier = Modifier.fillMaxWidth()) {
            SettingRepository.SongSortType.values().forEach { type ->
                ItemCheckbox(
                    icon = {
                        Icon(
                            imageVector = type.itemIcon,
                            contentDescription = type.itemName
                        )
                    },
                    text = { Text(text = type.itemName) },
                    subText = { },
                    checked = SettingRepository.SongSort == type.value,
                    onCheckedChange = {
                        SettingRepository.SongSort = type.value
                    }
                )
            }
        }
    }
}