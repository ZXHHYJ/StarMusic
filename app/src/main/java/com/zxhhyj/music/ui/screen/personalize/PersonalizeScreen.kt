package com.zxhhyj.music.ui.screen.personalize

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FontDownload
import androidx.compose.material.icons.rounded.Label
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSwitcher
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.rememberNavController

@Composable
fun PersonalizeScreen(
    paddingValues: PaddingValues,
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<SheetDestination>
) {
    AppScaffold(
        topBar = {
            AppCenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.personalize)
            ) {

            }
        }, modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Label,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.sound_quality_label)) },
                        subText = { Text(text = stringResource(id = R.string.sound_quality_label_info)) },
                        checked = SettingRepository.EnableShowSoundQualityLabel,
                        onCheckedChange = {
                            SettingRepository.EnableShowSoundQualityLabel = it
                        }
                    )
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    val easterEggSong =
                        AndroidMediaLibRepository.songs.find {
                            it.songName == "那一天从梦中醒来" && it.artist.name.contains(
                                "双笙"
                            )
                        }
                    SongItem(
                        sheetNavController = easterEggSong?.let { sheetNavController }
                            ?: rememberNavController(initialBackstack = emptyList()),
                        songBean = easterEggSong ?: SongBean.Local(
                            coverUrl = "",
                            SongBean.Album("未知彩蛋歌曲专辑"),
                            SongBean.Artist("未知彩蛋歌曲作者"),
                            0,
                            "",
                            0,
                            "未知彩蛋歌曲名称",
                            0,
                            0,
                            0,
                            0,
                            null,
                            0,
                            0
                        )
                    ) {
                        if (easterEggSong != null) {
                            PlayManager.play(listOf(easterEggSong), 0)
                        }
                    }
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemArrowRight(
                        icon = { Icon(imageVector = Icons.Rounded.FontDownload, null) },
                        text = { Text(text = stringResource(id = R.string.lyric)) },
                        subText = { }) {
                        mainNavController.navigate(ScreenDestination.Lyric)
                    }
                }
            }
        }
    }
}