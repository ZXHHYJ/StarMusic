package com.zxhhyj.music.ui.screen.play

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.common.KeepScreenOn
import com.zxhhyj.music.ui.common.Lyric
import com.zxhhyj.music.ui.theme.translucentWhiteColor

@Composable
fun ColumnScope.PlayLyricScreen() {
    val pause by PlayManager.pauseLiveData().observeAsState(true)
    KeepScreenOn(enable = !pause)
    val song by PlayManager.currentSongLiveData().observeAsState()
    if (song?.lyric == null || song?.lyric?.isEmpty() == true) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            Text(
                text = stringResource(id = R.string.not_lyric),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        val liveTime by PlayManager.progressLiveData()
            .observeAsState(0)
        Lyric(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f),
            lyric = song!!.lyric!!,
            liveTime = liveTime,
            translation = SettingRepository.EnableLyricsTranslation,
            lyricItem = { modifier: Modifier,
                          model: String,
                          index: Int,
                          position: Int ->
                Text(
                    modifier = modifier
                        .padding(
                            vertical = 18.dp,
                            horizontal = PlayScreen.PlayScreenContentHorizontal
                        ),
                    text = model,
                    color = if (position == index) Color.White else translucentWhiteColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        ) {
            PlayManager.seekTo(it)
            PlayManager.start()
        }

    }
}