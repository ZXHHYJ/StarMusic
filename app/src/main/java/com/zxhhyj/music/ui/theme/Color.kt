package com.zxhhyj.music.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kyant.monet.a1

val appAccentColor
    @Composable get() = 45.a1

val appIconAccentColor
    @Composable get() = appAccentColor

val appTextAccentColor
    @Composable get() = appAccentColor

val appTextButtonAccentColor
    @Composable get() = appAccentColor

val appButtonAccentColor
    @Composable get() = appAccentColor.copy(alpha = 0.7f)

/**
 * 标准字体颜色
 */
val textColor: Color
    @Composable get() = Color.Black

/**
 * 标准字体颜色的反色
 */
val onTextColor: Color
    @Composable get() = Color.White

/**
 * 标准字体颜色的辅助色
 */
val textColorLight
    @Composable get() = Color(0xff7a7a7a)

/**
 * 半透明白色的文字
 */
val translucentWhite = Color(0x80FFFFFF)

/**
 * translucentWhite没有效果时使用
 */
val translucentWhiteFixBug = Color(0x33FFFFFF)

/**
 * app背景色
 */
val appBackgroundColor
    @Composable get() = Color.White

/**
 * app背景色的反色
 */
val onBackground
    @Composable get() = Color.Black

/**
 * 默认分割线的颜色
 */
val appDividerColor
    @Composable get() = Color(0xd8d8d8d8)