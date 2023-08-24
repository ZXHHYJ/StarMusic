package com.zxhhyj.ui.view.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles
import com.zxhhyj.ui.theme.StarDimens

@Composable
fun ItemCheckbox(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    subText: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
) {
    Row(
        modifier = Modifier
            .clickable {
                onCheckedChange.invoke(!checked)
            }
            .fillMaxWidth()
            .padding(horizontal = StarDimens.horizontal, vertical = StarDimens.vertical),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalContentColor provides LocalColorScheme.current.highlight) {
            icon()
        }
        Spacer(modifier = Modifier.width(StarDimens.vertical))
        Column(verticalArrangement = Arrangement.Center) {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyles.current.main,
                LocalContentColor provides LocalColorScheme.current.text
            ) {
                text()
            }
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyles.current.sub,
                LocalContentColor provides LocalColorScheme.current.subText
            ) {
                subText()
            }
        }
        Spacer(modifier = Modifier.weight(1.0f))
        if (checked) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = LocalColorScheme.current.highlight
            )
        }
    }
}
