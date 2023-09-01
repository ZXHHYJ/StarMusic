package com.zxhhyj.music.ui.screen.misc

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemSwitcher

@Composable
fun MiscScreen(paddingValues: PaddingValues) {
    AppScaffold(
        topBar = {
            AppCenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.misc)
            )
        }, modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues)
    ) {
        LazyColumn() {
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
                }
            }
        }
    }
}