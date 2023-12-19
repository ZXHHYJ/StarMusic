package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Abc
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemCheckbox
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSubTitle
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

private val SettingRepository.SongSortEnum.itemName: String
    @Composable get() = when (this) {
        SettingRepository.SongSortEnum.SONG_NAME -> stringResource(id = R.string.song_name)
        SettingRepository.SongSortEnum.SONG_DURATION -> stringResource(id = R.string.song_duration)
        SettingRepository.SongSortEnum.SINGER_NAME -> stringResource(id = R.string.singer_name)
        SettingRepository.SongSortEnum.DATE_MODIFIED -> stringResource(id = R.string.date_modified)
    }

private val SettingRepository.SongSortEnum.itemIcon: ImageVector
    @Composable get() = when (this) {
        SettingRepository.SongSortEnum.SONG_NAME -> Icons.Rounded.Abc
        SettingRepository.SongSortEnum.SONG_DURATION -> Icons.Rounded.Timer
        SettingRepository.SongSortEnum.SINGER_NAME -> Icons.Rounded.Person
        SettingRepository.SongSortEnum.DATE_MODIFIED -> Icons.Rounded.AccessTime
    }

@Composable
fun MediaLibMenuSheet(dialogNavController: NavController<DialogDestination>) {
    LazyColumn {
        item {
            ItemSubTitle {
                Text(text = stringResource(id = R.string.shortcut_add))
            }
        }
        item {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                Item(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.refresh_all_media_lib)) },
                    subText = { },
                    enabled = SettingRepository.EnableAndroidMediaLibs || SettingRepository.EnableWebDav
                ) {
                    if (SettingRepository.EnableAndroidMediaLibs) {
                        dialogNavController.navigate(DialogDestination.RefreshAndroidMediaLib)
                    }
                    if (SettingRepository.EnableWebDav) {
                        dialogNavController.navigate(DialogDestination.RefreshWebDavMediaLib)
                    }
                }
            }
        }
        item {
            ItemSubTitle {
                Text(text = stringResource(id = R.string.sort_by))
            }
        }
        item {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                SettingRepository.SongSortEnum.entries.forEachIndexed { index, type ->
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
                    if (index != SettingRepository.SongSortEnum.entries.size - 1) {
                        ItemDivider()
                    }
                }
            }
        }
    }
}