package com.zxhhyj.music.ui.screen.play

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.common.KeepScreenOn
import com.zxhhyj.music.ui.common.lyric.Lyric
import com.zxhhyj.music.ui.theme.translucentWhiteColor

@Composable
fun ColumnScope.PlayLyricScreen() {
    val pause by PlayManager.pauseLiveData().observeAsState(true)
    KeepScreenOn(enable = !pause)
    val song by PlayManager.currentSongLiveData().observeAsState()
    val liveTime by PlayManager.progressLiveData()
        .observeAsState(0)
    Lyric(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1.0f),
        lyric = song?.lyric ?: "[00:00.00]没有歌词",
        liveTime = liveTime,
        translation = SettingRepository.EnableLyricsTranslation,
        lyricItem = { modifier: Modifier,
                      model: String,
                      index: Int,
                      position: Int ->
            val fontSize = 20
            Text(
                modifier = modifier
                    .padding(
                        vertical = 18.dp,
                        horizontal = PlayScreen.PlayScreenContentHorizontal
                    ),
                text = model,
                color = if (position == index) Color.White else translucentWhiteColor,
                fontSize = (fontSize + fontSize * SettingRepository.lyricFontSize).sp,
                fontWeight = if (SettingRepository.lyricFontBold) FontWeight.Bold else null
            )
        }
    ) {
        PlayManager.seekTo(it)
        PlayManager.start()
    }
}