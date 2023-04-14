package com.zxhhyj.music.ui.screen.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.ui.composable.TopAppBar
import com.zxhhyj.music.ui.composable.bindTopAppBarState
import com.zxhhyj.music.ui.composable.rememberTopAppBarState
import com.zxhhyj.music.ui.screen.setting.SettingSwitchItem

@Composable
fun ThemeScreen(padding: PaddingValues) {
    val topAppBarState = rememberTopAppBarState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .bindTopAppBarState(topAppBarState)
    ) {
        item {
            SettingSwitchItem(
                imageVector = Icons.Rounded.Album,
                title = "封面取色",
                subTitle = "根据当前播放歌曲的封面取色",
                checked = SettingRepository.enableAlbumGetColor,
                onCheckedChange = {
                    SettingRepository.enableAlbumGetColor = it
                }
            )
        }
    }
    TopAppBar(
        state = topAppBarState, modifier = Modifier, title = stringResource(id = R.string.theme)
    )
}