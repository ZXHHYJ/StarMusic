package com.zxhhyj.ui.theme

import androidx.compose.ui.graphics.Color

data class ColorScheme(
    val highlight: Color,
    val text: Color = Color.Black,
    val onText: Color = Color.White,
    val subText: Color = Color(0xFF7A7A7A),
    val background: Color = Color(0xfff1f0f5),
    val highBackground: Color = Color.White,
    val outline: Color = Color(0xFFDFDFDF),
    val disabled: Color = Color(0xFF8D8D8D)
)