package com.zxhhyj.music.ui.common.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zxhhyj.music.ui.theme.appButtonAccentColor
import com.zxhhyj.music.ui.theme.onTextColor
import com.zxhhyj.music.ui.theme.roundShape

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable (RowScope.() -> Unit)
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        shape = roundShape,
        border = null,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = appButtonAccentColor,
            contentColor = onTextColor
        ),
        contentPadding = contentPadding,
        content = content
    )
}