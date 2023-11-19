package com.zxhhyj.music.service.playermanager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object PlayerTimerManager {
    sealed class TimerType {
        data object Off : TimerType()
        data object Minutes15 : TimerType()
        data object Minutes30 : TimerType()
        data object Minutes45 : TimerType()
        data object Minutes60 : TimerType()
        data object CustomMinutes : TimerType()
    }

    var currentTimerType by mutableStateOf<TimerType>(TimerType.Off)
        private set

    var currentRemainingTime by mutableIntStateOf(0)
        private set

    private var timerJob: Job? = null

    fun startMinutes15Timer() {
        currentTimerType = TimerType.Minutes15
        customTimer(15 * 60 * 1000)
    }

    fun startMinutes30Timer() {
        currentTimerType = TimerType.Minutes30
        customTimer(30 * 60 * 1000)
    }

    fun startMinutes45Timer() {
        currentTimerType = TimerType.Minutes45
        customTimer(45 * 60 * 1000)
    }

    fun startMinutes60Timer() {
        currentTimerType = TimerType.Minutes60
        customTimer(60 * 60 * 1000)
    }

    fun startCustomTimer(durationInSeconds: Int) {
        currentTimerType = TimerType.CustomMinutes
        customTimer(durationInSeconds)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun customTimer(durationInSeconds: Int) {
        timerJob = timerJob?.cancel().run {
            GlobalScope.launch(Dispatchers.IO) {
                flow {
                    var remainingTimeInSeconds = durationInSeconds
                    while (true) {
                        emit(remainingTimeInSeconds)
                        delay(1000)
                        remainingTimeInSeconds -= 1000
                    }
                }.collect {
                    currentRemainingTime = it
                    if (it <= 0) {
                        withContext(Dispatchers.Main) {
                            PlayerManager.clearPlayList()
                        }
                        cancelTimer()
                    }
                }
            }
        }
    }

    fun cancelTimer() {
        currentTimerType = TimerType.Off
        timerJob?.cancel()
    }

}