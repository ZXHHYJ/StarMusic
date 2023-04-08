package studio.mandysa.music.ui.sheet.songinfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import studio.mandysa.music.R
import studio.mandysa.music.logic.helper.CopyHelper
import studio.mandysa.music.logic.helper.ToastHelper
import studio.mandysa.music.ui.theme.appTextAccentColor
import studio.mandysa.music.ui.theme.horizontal
import studio.mandysa.music.ui.theme.textColorLight
import studio.mandysa.music.ui.theme.vertical

@Composable
fun SongInfoItem(title: String, info: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(horizontal = horizontal, vertical = vertical)) {
        Text(text = title, color = appTextAccentColor)
        Spacer(modifier = Modifier.width(horizontal))
        Spacer(modifier = Modifier.weight(1.0f))
        Text(text = info, color = textColorLight, modifier = Modifier.clickable {
            CopyHelper.copyText(info)
            ToastHelper.toast(R.string.copyed)
        })
    }
}