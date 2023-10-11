package com.zxhhyj.music.ui.screen.lyric

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatBold
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSlider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSwitcher

@Composable
fun LyricScreen(paddingValues: PaddingValues) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppCenterTopBar(
                modifier = Modifier, title = stringResource(id = R.string.lyric)
            )
        }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
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
                        subText = {},
                        checked = SettingRepository.EnableLyricsTranslation,
                        onCheckedChange = {
                            SettingRepository.EnableLyricsTranslation = it
                        }
                    )
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemSlider(
                        text = {
                            Text(text = stringResource(id = R.string.lyric_font_size))
                        },
                        subText = {
                            val fontSize = 20
                            Text(text = "${(fontSize + fontSize * SettingRepository.lyricFontSize).toInt()}")
                        },
                        value = SettingRepository.lyricFontSize.toFloat(), onValueChange = {
                            SettingRepository.lyricFontSize = "%.1f".format(it).toDouble()
                        })
                    ItemDivider()
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.FormatBold,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.lyric_font_bold)) },
                        subText = { },
                        checked = SettingRepository.lyricFontBold,
                        onCheckedChange = {
                            SettingRepository.lyricFontBold = it
                        }
                    )
                }
            }
        }
    }
}