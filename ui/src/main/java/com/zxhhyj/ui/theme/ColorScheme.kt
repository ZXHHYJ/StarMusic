package com.zxhhyj.ui.theme

import androidx.compose.ui.graphics.Color

data class ColorScheme(
    val highlight: Color,
    val text: Color = Color.Black,
    val onText: Color = Color.White,
    val subText: Color = Color(0xFF7A7A7A),
    val background: Color = Color.White,
    val onBackground: Color = Color.Black,
    val subBackground: Color = Color(0xfff1f0f5),
    val onSubBackground: Color = Color.White,
    val outline: Color = Color(0xFFDFDFDF)
)