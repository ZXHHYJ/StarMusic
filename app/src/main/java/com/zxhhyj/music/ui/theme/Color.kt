package com.zxhhyj.music.ui.theme

import androidx.compose.ui.graphics.Color
import com.zxhhyj.ui.ColorScheme

/**
 * 半透明白色的文字
 */
val translucentWhiteColor = Color(0x80FFFFFF)

/**
 * translucentWhite没有效果时使用
 */
val translucentWhiteFixBugColor = Color(0x33FFFFFF)

val lightColorScheme = ColorScheme(
    highlight = Color.Red,
    text = Color.Black,
    onText = Color.White,
    subText = Color(0xff7a7a7a),
    background = Color.White,
    onBackground = Color.Black,
    outline = Color(0xd8d8d8d8)
)