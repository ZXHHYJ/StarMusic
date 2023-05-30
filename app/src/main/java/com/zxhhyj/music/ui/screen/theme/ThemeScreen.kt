package com.zxhhyj.music.ui.screen.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.ui.common.TopAppBar
import com.zxhhyj.music.ui.common.bindTopAppBarState
import com.zxhhyj.music.ui.common.rememberTopAppBarState
import com.zxhhyj.music.ui.item.SettingSwitchItem

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
                imageVector = Icons.Rounded.ColorLens,
                title = stringResource(id = R.string.monet_get_color),
                subTitle = stringResource(id = R.string.monet_get_color_info),
                checked = SettingRepository.EnableMonet,
                onCheckedChange = {
                    SettingRepository.EnableMonet = it
                }
            )
        }
        item {
            SettingSwitchItem(
                imageVector = Icons.Rounded.Album,
                title = stringResource(id = R.string.album_get_color),
                subTitle = stringResource(id = R.string.album_get_color_info),
                checked = SettingRepository.EnableAlbumGetColor,
                onCheckedChange = {
                    SettingRepository.EnableAlbumGetColor = it
                }
            )
        }
    }
    TopAppBar(
        state = topAppBarState, modifier = Modifier, title = stringResource(id = R.string.theme)
    )
}