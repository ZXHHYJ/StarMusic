package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.utils.toTimeString
import com.zxhhyj.music.service.playermanager.PlayerTimerManager
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemCheckbox
import com.zxhhyj.ui.view.item.ItemDivider
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

private val PlayerTimerManager.TimerType.itemName
    @Composable get() = when (this) {
        PlayerTimerManager.TimerType.CustomMinutes -> stringResource(id = R.string.custom)
        PlayerTimerManager.TimerType.Minutes15 -> stringResource(id = R.string.minutes_15)
        PlayerTimerManager.TimerType.Minutes30 -> stringResource(id = R.string.minutes_30)
        PlayerTimerManager.TimerType.Minutes45 -> stringResource(id = R.string.minutes_45)
        PlayerTimerManager.TimerType.Minutes60 -> stringResource(id = R.string.minutes_60)
        PlayerTimerManager.TimerType.Off -> stringResource(id = R.string.offed)
    }


@Composable
fun TimerSheet(dialogNavController: NavController<DialogDestination>) {
    RoundColumn(modifier = Modifier.fillMaxWidth()) {
        val timerTypeList = listOf(
            PlayerTimerManager.TimerType.Off,
            PlayerTimerManager.TimerType.Minutes15,
            PlayerTimerManager.TimerType.Minutes30,
            PlayerTimerManager.TimerType.Minutes45,
            PlayerTimerManager.TimerType.Minutes60,
            PlayerTimerManager.TimerType.CustomMinutes
        )
        timerTypeList.forEachIndexed { index, timerType ->
            ItemCheckbox(
                icon = {},
                text = { Text(text = timerType.itemName) },
                subText = {
                    when (timerType) {
                        PlayerTimerManager.TimerType.Off -> {}

                        else -> {
                            if (PlayerTimerManager.currentTimerType == timerType) {
                                Text(text = PlayerTimerManager.currentRemainingTime.toTimeString())
                            }
                        }
                    }
                },
                checked = PlayerTimerManager.currentTimerType == timerType,
                onCheckedChange = {
                    if (it) {
                        when (timerType) {
                            PlayerTimerManager.TimerType.CustomMinutes -> {
                                dialogNavController.navigate(DialogDestination.CustomTimer)
                            }

                            PlayerTimerManager.TimerType.Minutes15 -> {
                                PlayerTimerManager.startMinutes15Timer()
                            }

                            PlayerTimerManager.TimerType.Minutes30 -> {
                                PlayerTimerManager.startMinutes30Timer()
                            }

                            PlayerTimerManager.TimerType.Minutes45 -> {
                                PlayerTimerManager.startMinutes45Timer()
                            }

                            PlayerTimerManager.TimerType.Minutes60 -> {
                                PlayerTimerManager.startMinutes60Timer()
                            }

                            PlayerTimerManager.TimerType.Off -> {
                                PlayerTimerManager.cancelTimer()
                            }
                        }
                    }
                }
            )
            if (index != timerTypeList.size - 1) {
                ItemDivider()
            }
        }
    }
}