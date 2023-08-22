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
    background = Color(0xFF070707),
    highBackground = Color(0xff1c1c1e),
    outline = Color(0xFF383838),
    disabled = Color(0xFF8D8D8D)
)