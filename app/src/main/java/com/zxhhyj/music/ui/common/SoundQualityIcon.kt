package com.zxhhyj.music.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.logic.bean.SongBean

@Composable
fun SoundQualityIcon(modifier: Modifier = Modifier, song: SongBean) {
    val fontSize = 8.sp
    Card(
        shape = RoundedCornerShape(2.dp),
        elevation = 0.dp,
        modifier = modifier.padding(end = 2.dp)
    ) {
        when (song.bitrate) {
            in 128..192 -> {
                Text(
                    text = "SQ",
                    modifier = modifier
                        .background(Color.Blue.copy(0.3f))
                        .padding(horizontal = 2.dp),
                    fontSize = fontSize
                )
            }
            in 256..320 -> {
                Text(
                    text = "HQ",
                    modifier = modifier
                        .background(Color.Red.copy(0.1f))
                        .padding(horizontal = 2.dp),
                    fontSize = fontSize
                )
            }
            in 320..Int.MAX_VALUE -> {
                Text(
                    text = "HR",
                    modifier = modifier
                        .background(Color.Yellow.copy(0.5f))
                        .padding(horizontal = 2.dp),
                    fontSize = fontSize
                )
            }
        }
    }
}