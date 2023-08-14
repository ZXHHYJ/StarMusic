@file:Suppress("UNUSED")

package com.zxhhyj.ui.view.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles
import com.zxhhyj.ui.theme.StarDimens

@Composable
fun Item(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    subText: @Composable () -> Unit,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = StarDimens.horizontal, vertical = StarDimens.vertical),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalContentColor provides if (enabled) LocalColorScheme.current.highlight else LocalColorScheme.current.disabled) {
            icon()
        }
        Spacer(modifier = Modifier.width(StarDimens.vertical))
        Column(verticalArrangement = Arrangement.Center) {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyles.current.main,
                LocalContentColor provides if (enabled) LocalColorScheme.current.text else LocalColorScheme.current.disabled
            ) {
                text()
            }
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyles.current.sub,
                LocalContentColor provides if (enabled) LocalColorScheme.current.subText else LocalColorScheme.current.disabled
            ) {
                subText()
            }
        }
    }
}