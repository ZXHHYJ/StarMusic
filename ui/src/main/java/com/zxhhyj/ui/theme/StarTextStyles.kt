package com.zxhhyj.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Immutable
class StarTextStyles(
    val main: TextStyle,
    val sub: TextStyle,
    val paragraph: TextStyle
)

fun starTextStyles(
    main: TextStyle = TextStyle(fontSize = 16.sp),
    sub: TextStyle = TextStyle(fontSize = 13.sp),
    paragraph: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 1.5f.em
    )
): StarTextStyles = StarTextStyles(
    main = main,
    sub = sub,
    paragraph = paragraph
)