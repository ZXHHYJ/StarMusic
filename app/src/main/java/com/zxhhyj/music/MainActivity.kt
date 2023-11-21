package com.zxhhyj.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            StarMusicTheme {
                MainScreen()
                ComposeToast()
            }
            LaunchedEffect(SettingRepository.EnableEqualizer) {
                PlayerManager.setEnableEqualizer(SettingRepository.EnableEqualizer)
            }
            val playMode by PlayerManager.playModeLiveData().observeAsState()
            LaunchedEffect(playMode) {
                playMode?.let {
                    SettingRepository.PlayMode = it
                }
            }
            if (SettingRepository.EnablePlayMemory) {
                val index by PlayerManager.indexLiveData().observeAsState()
                val playlist by PlayerManager.playListLiveData().observeAsState()
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