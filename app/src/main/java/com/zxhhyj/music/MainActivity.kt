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
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.ComposeToast.ComposeToast
import com.zxhhyj.music.ui.screen.main.MainScreen
import com.zxhhyj.music.ui.theme.StarMusicTheme


class MainActivity : ComponentActivity() {

    private var headphoneReceiver: HeadphoneReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        headphoneReceiver = HeadphoneReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG)
        registerReceiver(headphoneReceiver, intentFilter)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            StarMusicTheme {
                MainScreen()
                ComposeToast()
            }
            LaunchedEffect(SettingRepository.EnableEqualizer) {
                PlayerManager.setEnableEqualizer(SettingRepository.EnableEqualizer)
            }
            LaunchedEffect(Unit) {
                PlayerManager.setPlayMode(SettingRepository.PlayMode)
                //恢复播放模式
            }
            val playMode by PlayerManager.playModeLiveData().observeAsState()
            LaunchedEffect(playMode) {
                if (playMode != null) {
                    SettingRepository.PlayMode = playMode as PlayerManager.PlayMode
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