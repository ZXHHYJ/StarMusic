package com.zxhhyj.music.ui.screen.play

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val animDurationMillis = 1000
    Lyric(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1.0f),
        lyric = song?.lyric,
        liveTime = liveTime,
        translation = SettingRepository.EnableLyricsTranslation,
        lyricScrollAnimationSpec = tween(animDurationMillis),
        lyricItem = { modifier: Modifier,
                      model: String,
                      index: Int,
                      position: Int ->
            val textColor by animateColorAsState(
                targetValue = if (position == index) Color.White else translucentWhiteColor,
                label = "lyric_text_color",
                animationSpec = tween(animDurationMillis)
            )
            val textScale by animateFloatAsState(
                targetValue = if (position == index) 1.05f else 1f,
                label = "lyric_text_scale",
                animationSpec = tween(animDurationMillis)
            )
            Text(
                modifier = modifier
                    .padding(
                        vertical = 18.dp,
                        horizontal = PlayScreen.PlayScreenContentHorizontal
                    )
                    .graphicsLayer {
                        scaleX = textScale
                        scaleY = textScale
                        transformOrigin = TransformOrigin(0f, 0f)
                    },
                text = model,
                color = textColor,
                fontSize = SettingRepository.lyricFontSize.sp,
                fontWeight = if (SettingRepository.lyricFontBold) FontWeight.Bold else null,
                textAlign = TextAlign.Left
            )
        }
    ) {
        PlayManager.seekTo(it)
        PlayManager.start()
    }
}