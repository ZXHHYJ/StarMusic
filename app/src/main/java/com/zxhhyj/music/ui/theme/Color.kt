package com.zxhhyj.music.ui.theme

import androidx.compose.ui.graphics.Color
import com.zxhhyj.ui.theme.ColorScheme

/**
 * 半透明白色的文字
 */
val translucentWhiteColor = Color(0x80FFFFFF)

/**
 * translucentWhite没有效果时使用
 */
val translucentWhiteFixBugColor = Color(0x33FFFFFF)

val lightColorScheme = ColorScheme(highlight = Color(0xFFE41308))

val nightColorScheme = ColorScheme(
    highlight = Color(0xFFE41308),
    text = Color.White,
    onText = Color.Black,
    subText = Color(0xFF7A7A7A),
    subBackground = Color(0xFF070707),
    background = Color.White,
    outline = Color(0xFF383838)
)