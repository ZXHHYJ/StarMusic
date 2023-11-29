package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Equalizer
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Lyrics
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.utils.toTimeString
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.service.playermanager.PlayerTimerManager
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemCheckbox
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.popAll

private val PlayerManager.PlayMode.itemIcon
    get() = when (this) {
        PlayerManager.PlayMode.SINGLE_LOOP -> Icons.Rounded.RepeatOne
        PlayerManager.PlayMode.LIST_LOOP -> Icons.Rounded.Repeat
        PlayerManager.PlayMode.RANDOM -> Icons.Rounded.Shuffle
    }

private val PlayerManager.PlayMode.itemName
    @Composable get() = when (this) {
        PlayerManager.PlayMode.SINGLE_LOOP -> stringResource(id = R.string.single_loop)
        PlayerManager.PlayMode.LIST_LOOP -> stringResource(id = R.string.list_loop)
        PlayerManager.PlayMode.RANDOM -> stringResource(id = R.string.random)
    }

@Composable
fun SongPanelSheet(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<SheetDestination>,
    songBean: SongBean
) {
    LazyColumn {
        item {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                SongItem(
                    songBean = songBean,
                    actions = {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowRight,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        sheetNavController.navigate(SheetDestination.SongMenu(songBean))
                    })
            }
        }
        item {
            ItemSpacer()
        }
        item {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                val currentPlayMode by PlayerManager.playModeFlow.collectAsState()
                PlayerManager.PlayMode.values().forEachIndexed { index, playMode ->
                    ItemCheckbox(
                        icon = {
                            Icon(
                                imageVector = playMode.itemIcon,
                                contentDescription = playMode.itemName
                            )
                        },
                        text = { Text(text = playMode.itemName) },
                        subText = { },
                        checked = currentPlayMode == playMode,
                        onCheckedChange = {
                            PlayerManager.setPlayMode(playMode)
                        }
                    )
                    if (index != PlayerManager.PlayMode.values().size - 1) {
                        ItemDivider()
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
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Timer,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.timer)) },
                    subText = {
                        when (PlayerTimerManager.currentTimerType) {
                            PlayerTimerManager.TimerType.Off -> {}
                            else -> {
                                Text(text = PlayerTimerManager.currentRemainingTime.toTimeString())
                            }
                        }
                    }) {
                    sheetNavController.navigate(SheetDestination.Timer)
                }
            }
        }
        item {
            ItemSpacer()
        }
        item {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                ItemArrowRight(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Lyrics,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.lyric)) },
                    subText = { }) {
                    sheetNavController.pop()
                    mainNavController.navigate(ScreenDestination.Lyric)
                }
                ItemDivider()
                ItemArrowRight(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Equalizer,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.eq)) },
                    subText = { }) {
                    sheetNavController.pop()
                    mainNavController.navigate(ScreenDestination.Equalizer)
                }
            }
        }
    }
}