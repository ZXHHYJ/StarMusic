package com.zxhhyj.music

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.receiver.HeadphoneReceiver
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.common.PopWindows
import com.zxhhyj.music.ui.screen.main.MainScreen
import com.zxhhyj.music.ui.theme.MandySaMusicTheme


class MainActivity : ComponentActivity() {

    private var headphoneReceiver: HeadphoneReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(MainApplication.uncaughtExceptionHandler)
        headphoneReceiver = HeadphoneReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG)
        registerReceiver(headphoneReceiver, intentFilter)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            MandySaMusicTheme {
                MainScreen()
                PopWindows.PopWin()
            }
            LaunchedEffect(SettingRepository.EnableEqualizer) {
                PlayManager.setEnableEqualizer(SettingRepository.EnableEqualizer)
            }
            LaunchedEffect(Unit) {
                PlayManager.setPlayMode(SettingRepository.PlayMode)
                //恢复播放模式
            }
            val playMode by PlayManager.playModeLiveData().observeAsState()
            LaunchedEffect(playMode) {
                if (playMode != null) {
                    SettingRepository.PlayMode = playMode as PlayManager.PlayMode
                }
                //播放模式变更后进行持久化
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(headphoneReceiver)
    }

}