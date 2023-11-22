package com.zxhhyj.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import com.zxhhyj.music.logic.bean.PlayMemoryBean
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.AudioBecomingNoisyManager
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.ComposeToast.ComposeToast
import com.zxhhyj.music.ui.screen.main.MainScreen
import com.zxhhyj.music.ui.theme.StarMusicTheme


class MainActivity : ComponentActivity() {

    private val audioBecomingNoisyManager by lazy {
        AudioBecomingNoisyManager(this) {
            PlayerManager.pause()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioBecomingNoisyManager.setEnabled(true)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            StarMusicTheme {
                MainScreen()
                ComposeToast()
            }
            LaunchedEffect(SettingRepository.EnableEqualizer) {
                PlayerManager.setEnableEqualizer(SettingRepository.EnableEqualizer)
            }
            val playMode by PlayerManager.playModeFlow.collectAsState()
            LaunchedEffect(playMode) {
                SettingRepository.PlayMode = playMode
            }
            if (SettingRepository.EnablePlayMemory) {
                val index by PlayerManager.indexFlow.collectAsState()
                val playlist by PlayerManager.playListFlow.collectAsState()
                LaunchedEffect(index, playlist) {
                    SettingRepository.PlayMemory = PlayMemoryBean(index, playlist)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioBecomingNoisyManager.setEnabled(false)
    }

}