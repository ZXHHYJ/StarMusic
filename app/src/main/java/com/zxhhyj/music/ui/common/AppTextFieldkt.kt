package com.zxhhyj.music.ui.common

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.music.ui.theme.appAccentColor
import com.zxhhyj.music.ui.theme.onBackground

@Composable
fun AppOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true, modifier = modifier,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = onBackground,
            unfocusedBorderColor = appAccentColor,
            cursorColor = appAccentColor
        )
    )
}