package com.zxhhyj.ui.view.item

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import com.zxhhyj.ui.theme.LocalColorScheme

@Composable
fun ItemArrowRight(
    icon: (@Composable () -> Unit)? = null,
    text: @Composable () -> Unit,
    subText: @Composable () -> Unit,
    actions: @Composable () -> Unit = {},
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Item(icon, text, subText, actions = {
        actions()
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = LocalColorScheme.current.subText
        )
    }, enabled, onClick)
}