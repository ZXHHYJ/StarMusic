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
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles

@Composable
fun SongInfoItem(title: String, info: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(horizontal = horizontal, vertical = vertical)) {
        Text(
            text = title,
            color = LocalColorScheme.current.highlight,
            style = LocalTextStyles.current.main
        )
        Spacer(modifier = Modifier.width(horizontal))
        Spacer(modifier = Modifier.weight(1.0f))
        Text(
            text = info,
            color = LocalColorScheme.current.subText,
            style = LocalTextStyles.current.main,
            modifier = Modifier.clickable {
                CopyUtils.copyText(info)
            })
    }
}