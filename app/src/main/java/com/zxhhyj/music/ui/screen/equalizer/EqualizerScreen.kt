package com.zxhhyj.music.ui.screen.equalizer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.zxhhyj.music.service.playmanager.PlayManager
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
            .statusBarsPadding()
            .padding(paddingValues), topBar = {
            AppCenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.eq)
            ) {

            }
        }) {
        Column(modifier = Modifier.fillMaxSize()) {
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
            ItemSpacer()
            if (SettingRepository.EnableEqualizer) {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {

                    val bandCount = PlayManager.getNumberOfBands()
                    val bandRange = PlayManager.getBandRange()

                    for (bandIndex in 0 until bandCount) {
                        var bandLevel by remember {
                            mutableIntStateOf(
                                SettingRepository.EqualizerConfig[bandIndex]
                            )
                        }

                        val bandFreqRange = PlayManager.getBandFreqRange(bandIndex)
                        val bandFreqText = "${bandFreqRange.min()}-${bandFreqRange.max()}"

                        ItemSlider(
                            text = { Text(text = bandFreqText) },
                            subText = { Text(text = "$bandLevel") },
                            value = bandLevel.toFloat(),
                            onValueChange = { newLevel ->
                                PlayManager.setBandLevel(bandIndex, newLevel.toInt())
                                bandLevel = PlayManager.getBandLevel(bandIndex)
                                SettingRepository.EqualizerConfig =
                                    SettingRepository.EqualizerConfig.copyOf().apply {
                                        set(bandIndex, bandLevel)
                                    }
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
