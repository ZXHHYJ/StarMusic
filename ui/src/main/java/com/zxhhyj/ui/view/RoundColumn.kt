package com.zxhhyj.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.StarDimens

@Composable
fun RoundColumn(modifier: Modifier, content: @Composable () -> Unit) {
    AppCard(
        backgroundColor = LocalColorScheme.current.onSubBackground,
        modifier = modifier.padding(horizontal = StarDimens.horizontal)
    ) {
        Column(modifier = modifier) {
            content.invoke()
        }
    }
}