package com.zxhhyj.music.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kyant.monet.a1
import com.kyant.monet.a2
import com.kyant.monet.n1
import com.kyant.monet.rangeTo

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

//字体颜色
val textColor: Color
    @Composable get() = 2.n1..Color.White

//字体颜色的反色
val onTextColor: Color
    @Composable get() = Color.White..Color.Black

//字体颜色的辅助色
val textColorLight
    @Composable get() = 40.n1..80.n1

//半透明白色的文字
val translucentWhite = Color(0x80FFFFFF)

//translucentWhite没有效果时使用
val translucentWhiteFixBug = Color(0x33FFFFFF)

//背景色
val appBackgroundColor
    @Composable get() = Color.White..1.n1

//背景色的反色
val onBackground
    @Composable get() = Color.Black..Color.LightGray

//容器颜色
val cardBackgroundColor
    @Composable get() = 90.a2..70.a2

val appDividerColor
    @Composable get() = Color(0xd8d8d8d8)