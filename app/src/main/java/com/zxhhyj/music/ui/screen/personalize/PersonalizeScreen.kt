package com.zxhhyj.music.ui.screen.personalize

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Label
import androidx.compose.material.icons.rounded.Lyrics
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.service.playermanager.PlayerManager
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
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.personalize)) })
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
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
                            it.songName == "那一天从梦中醒来" && it.artistName?.contains("双笙") ?: false
                        }
                    SongItem(
                        sheetNavController = easterEggSong?.let { sheetNavController }
                            ?: rememberNavController(initialBackstack = emptyList()),
                        songBean = easterEggSong ?: SongBean.Local(
                            coverUrl = "",
                            "Nothing",
                            "Nothing",
                            null,
                            "",
                            0,
                            "Nothing",
                            0,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    ) {
                        if (easterEggSong != null) {
                            PlayerManager.play(listOf(easterEggSong), 0)
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
                        icon = { Icon(imageVector = Icons.Rounded.Lyrics, null) },
                        text = { Text(text = stringResource(id = R.string.lyric)) },
                        subText = { }) {
                        mainNavController.navigate(ScreenDestination.Lyric)
                    }
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemArrowRight(
                        icon = { Icon(imageVector = Icons.Rounded.Palette, null) },
                        text = { Text(text = stringResource(id = R.string.theme)) },
                        subText = { }) {
                        mainNavController.navigate(ScreenDestination.Theme)
                    }
                }
            }
        }
    }
}