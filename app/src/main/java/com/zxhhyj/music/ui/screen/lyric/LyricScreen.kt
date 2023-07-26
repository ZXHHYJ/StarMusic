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
import com.zxhhyj.music.ui.common.AppScaffold
import com.zxhhyj.music.ui.common.AppTopBar
import com.zxhhyj.music.ui.item.SettingSwitchItem

@Composable
fun LyricScreen(padding: PaddingValues) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = {
            AppTopBar(
                modifier = Modifier, title = stringResource(id = R.string.lyric)
            )
        }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
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
    }
}