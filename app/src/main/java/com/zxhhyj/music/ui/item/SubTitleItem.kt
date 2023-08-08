package com.zxhhyj.music.ui.item

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.LocalColorScheme

@Composable
fun SubTitleItem(modifier: Modifier = Modifier, title: String) {
    Text(
        text = title,
        color = LocalColorScheme.current.text,
        fontSize = 18.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontal, vertical = vertical)
    )
}