package com.zxhhyj.ui.view.item

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import com.zxhhyj.ui.theme.LocalColorScheme

@Composable
fun ItemCheckbox(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    subText: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
) {
    Item(icon = icon, text = text, subText = subText, actions = {
        if (checked) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = LocalColorScheme.current.highlight
            )
        }
    }) {
        onCheckedChange.invoke(!checked)
    }
}
