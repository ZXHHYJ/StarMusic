package com.zxhhyj.music.ui.screen.lyric

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Translate
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
fun LyricScreen(padding: PaddingValues) {
    val appTopBarState = rememberAppTopBarState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .bindAppTopBarState(appTopBarState)
    ) {
        item {
            SettingSwitchItem(
                imageVector = Icons.Rounded.Translate,
                title = stringResource(id = R.string.lyrics_translation),
                subTitle = stringResource(id = R.string.lyrics_translation),
                checked = SettingRepository.EnableLyricsTranslation,
                onCheckedChange = {
                    SettingRepository.EnableLyricsTranslation = it
                }
            )
        }
    }
    AppTopBar(
        state = appTopBarState, modifier = Modifier, title = stringResource(id = R.string.lyric)
    )
}