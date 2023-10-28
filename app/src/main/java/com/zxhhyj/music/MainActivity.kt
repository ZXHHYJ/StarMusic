package com.zxhhyj.music

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.zxhhyj.music.receiver.HeadphoneReceiver
import com.zxhhyj.music.ui.common.POPWindows
import com.zxhhyj.music.ui.screen.main.MainScreen
import com.zxhhyj.music.ui.theme.MandySaMusicTheme


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
            MandySaMusicTheme {
                MainScreen()
                POPWindows.PopWin()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(headphoneReceiver)
    }

}