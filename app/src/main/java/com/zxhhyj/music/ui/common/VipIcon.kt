package com.zxhhyj.music.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.ui.theme.LocalColorScheme

@Composable
fun VipIcon(modifier: Modifier = Modifier) {
    val fontSize = 8.sp
    Card(
        shape = RoundedCornerShape(2.dp),
        backgroundColor = LocalColorScheme.current.background,
        elevation = 0.dp,
        modifier = modifier
    ) {
        Text(
            text = "PRO",
            modifier = modifier.padding(horizontal = 2.dp),
            fontSize = fontSize,
            color = LocalColorScheme.current.highlight
        )
    }
}