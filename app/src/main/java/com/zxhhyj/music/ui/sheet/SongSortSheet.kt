package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Abc
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemCheckbox

val SettingRepository.SongSortType.itemName: String
    @Composable get() = when (this) {
        SettingRepository.SongSortType.NAME -> "歌曲名称"
        SettingRepository.SongSortType.DURATION -> "歌曲时长"
    }

val SettingRepository.SongSortType.itemIcon: ImageVector
    @Composable get() = when (this) {
        SettingRepository.SongSortType.NAME -> Icons.Rounded.Abc
        SettingRepository.SongSortType.DURATION -> Icons.Rounded.Timer
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