package com.zxhhyj.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles
import com.zxhhyj.ui.theme.StarDimens

@Composable
fun YesNoDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    title: String,
    confirm: @Composable () -> Unit,
    dismiss: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest, properties = properties) {
        AppCard(backgroundColor = LocalColorScheme.current.background) {
            Column(
                modifier = Modifier.padding(
                    horizontal = StarDimens.horizontal,
                    vertical = StarDimens.vertical
                )
            ) {
                Text(
                    text = title,
                    color = LocalColorScheme.current.highlight,
                    style = LocalTextStyles.current.main
                )
                Spacer(modifier = Modifier.height(StarDimens.vertical))
                content.invoke()
                Spacer(modifier = Modifier.height(StarDimens.vertical))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        StarDimens.horizontal,
                        Alignment.End
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides LocalColorScheme.current.highlight,
                        LocalTextStyle provides LocalTextStyles.current.main
                    ) {
                        dismiss.invoke()
                        confirm.invoke()
                    }
                }
            }
        }
    }
}