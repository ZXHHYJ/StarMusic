package com.zxhhyj.music.ui.screen.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.ui.common.AppTopBar
import com.zxhhyj.music.ui.common.bindAppTopBarState
import com.zxhhyj.music.ui.common.rememberAppTopBarState
import com.zxhhyj.music.ui.item.SettingSwitchItem

@Composable
fun ThemeScreen(padding: PaddingValues) {
    val appTopBarState = rememberAppTopBarState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .bindAppTopBarState(appTopBarState)
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
    }
    AppTopBar(
        state = appTopBarState, modifier = Modifier, title = stringResource(id = R.string.theme)
    )
}