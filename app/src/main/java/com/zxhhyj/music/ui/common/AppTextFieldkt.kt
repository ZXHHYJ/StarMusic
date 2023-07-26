package com.zxhhyj.music.ui.common

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.music.ui.theme.appBackgroundColor
import com.zxhhyj.music.ui.theme.appDividerColor
import com.zxhhyj.music.ui.theme.onBackground
import com.zxhhyj.music.ui.theme.textColorLight

@Composable
fun AppOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = appDividerColor,
            unfocusedBorderColor = appDividerColor,
            cursorColor = onBackground,
            backgroundColor = appBackgroundColor,
            placeholderColor = textColorLight
        ),
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}