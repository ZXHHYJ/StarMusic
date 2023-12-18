package com.zxhhyj.music.ui.screen.misc

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSwitcher

@Composable
fun MiscScreen(paddingValues: PaddingValues) {
    AppScaffold(
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.misc)) })
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.auto_play_music)) },
                        subText = { Text(text = stringResource(id = R.string.auto_play_music_info)) },
                        checked = SettingRepository.EnableAutoPlayMusic,
                        onCheckedChange = {
                            SettingRepository.EnableAutoPlayMusic = it
                        }
                    )
                    ItemDivider()
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Pause,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.is_playing_with_other_apps)) },
                        subText = { },
                        checked = SettingRepository.EnableIsPlayingWithOtherApps,
                        onCheckedChange = {
                            SettingRepository.EnableIsPlayingWithOtherApps = it
                        }
                    )
                    ItemDivider()
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Save,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.play_memory)) },
                        subText = { },
                        checked = SettingRepository.EnablePlayMemory,
                        onCheckedChange = {
                            SettingRepository.EnablePlayMemory = it
                        }
                    )
                }
            }
        }
    }
}