package com.zxhhyj.music.ui.screen.equalizer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Equalizer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSlider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSwitcher

@Composable
fun EqualizerScreen(paddingValues: PaddingValues) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.eq)) })
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Equalizer,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.eq)) },
                        subText = { },
                        checked = SettingRepository.EnableEqualizer,
                        onCheckedChange = {
                            SettingRepository.EnableEqualizer = it
                        }
                    )
                }
            }
            item {
                ItemSpacer()
            }
            item {
                if (SettingRepository.EnableEqualizer) {
                    RoundColumn(modifier = Modifier.fillMaxWidth()) {

                        val bandCount = PlayerManager.getNumberOfBands()
                        val bandRange = PlayerManager.getBandRange()

                        if (SettingRepository.EqualizerConfig == null) {
                            SettingRepository.EqualizerConfig = IntArray(bandCount)
                        }

                        for (bandIndex in 0 until bandCount) {
                            var bandLevel by remember {
                                mutableIntStateOf(
                                    SettingRepository.EqualizerConfig?.get(bandIndex) ?: 0
                                )
                            }
                            val bandFreqRange = PlayerManager.getBandFreqRange(bandIndex)
                            ItemSlider(
                                text = { Text(text = "${bandFreqRange.min() / 1000}Hz") },
                                subText = { Text(text = "${bandLevel / 100}db") },
                                value = bandLevel.toFloat(),
                                onValueChange = { newLevel ->
                                    PlayerManager.setBandLevel(bandIndex, newLevel.toInt())
                                    bandLevel = PlayerManager.getBandLevel(bandIndex)
                                    SettingRepository.EqualizerConfig =
                                        SettingRepository.EqualizerConfig?.copyOf()
                                            ?.apply { set(bandIndex, bandLevel) }
                                },
                                valueRange = bandRange.lower.toFloat()..bandRange.upper.toFloat()
                            )

                            if (bandIndex != bandCount - 1) {
                                ItemDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}
