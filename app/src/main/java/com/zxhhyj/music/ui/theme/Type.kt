package com.zxhhyj.music.ui.theme

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable

/**
 * 默认TextField样式
 */
val TextFieldColors
    @Composable get() = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = appDividerColor,
        unfocusedBorderColor = appDividerColor,
        cursorColor = onBackground,
        backgroundColor = appBackgroundColor,
        placeholderColor = textColorLight
    )