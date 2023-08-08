package com.zxhhyj.music.ui.screen.lyric

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.view.Scaffold
import com.zxhhyj.ui.view.TopBar
import com.zxhhyj.ui.view.item.ItemSwitcher

@Composable
fun LyricScreen(padding: PaddingValues) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = {
            TopBar(
                modifier = Modifier, title = stringResource(id = R.string.lyric)
            )
        }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                ItemSwitcher(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Translate,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(text = stringResource(id = R.string.lyrics_translation))
                    },
                    subText = {
                        Text(text = stringResource(id = R.string.lyrics_translation))
                    },
                    checked = SettingRepository.EnableLyricsTranslation,
                    onCheckedChange = {
                        SettingRepository.EnableLyricsTranslation = it
                    }
                )
            }
        }
    }
}