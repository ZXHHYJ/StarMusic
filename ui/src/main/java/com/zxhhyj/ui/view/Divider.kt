package com.zxhhyj.ui.view

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zxhhyj.ui.theme.LocalColorScheme

@Composable
fun AppDivider(
    modifier: Modifier,
    color: Color = LocalColorScheme.current.outline,
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness,
        startIndent = startIndent
    )
}