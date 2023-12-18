package com.zxhhyj.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
    positive: @Composable () -> Unit,
    negative: @Composable () -> Unit,
    neutral: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest, properties = properties) {
        AppCard(backgroundColor = LocalColorScheme.current.highBackground) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = StarDimens.horizontal,
                        vertical = StarDimens.vertical
                    ), verticalArrangement = Arrangement.spacedBy(StarDimens.vertical)
            ) {
                Text(
                    text = title,
                    color = LocalColorScheme.current.highlight,
                    style = LocalTextStyles.current.main
                )
                content.invoke(this)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(StarDimens.horizontal),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides LocalColorScheme.current.highlight,
                        LocalTextStyle provides LocalTextStyles.current.main
                    ) {
                        neutral?.invoke()
                        Spacer(modifier = Modifier.weight(1.0f))
                        negative.invoke()
                        positive.invoke()
                    }
                }
            }
        }
    }
}