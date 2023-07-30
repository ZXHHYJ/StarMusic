package com.zxhhyj.music.ui.sheet.songinfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.music.logic.utils.CopyUtils
import com.zxhhyj.music.ui.theme.appTextAccentColor
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.textColorLight
import com.zxhhyj.music.ui.theme.vertical

@Composable
fun SongInfoItem(title: String, info: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(horizontal = horizontal, vertical = vertical)) {
        Text(text = title, color = appTextAccentColor)
        Spacer(modifier = Modifier.width(horizontal))
        Spacer(modifier = Modifier.weight(1.0f))
        Text(text = info, color = textColorLight, modifier = Modifier.clickable {
            CopyUtils.copyText(info)
        })
    }
}