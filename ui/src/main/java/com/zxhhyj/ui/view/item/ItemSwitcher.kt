package com.zxhhyj.ui.view.item

import androidx.compose.runtime.Composable
import com.zxhhyj.ui.view.AppSwitch

@Composable
fun ItemSwitcher(
    icon: (@Composable () -> Unit)? = null,
    text: @Composable () -> Unit,
    subText: @Composable () -> Unit,
    enabled: Boolean = true,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
) {
    Item(icon = icon, text = text, subText = subText, enabled = enabled, actions = {
        if (enabled) {
            AppSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }) {
        onCheckedChange.invoke(!checked)
    }
}

