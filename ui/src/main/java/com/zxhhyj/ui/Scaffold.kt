package com.zxhhyj.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier

val LocalTopBarState = compositionLocalOf { TopBarState() }

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalTopBarState provides rememberSaveable { TopBarState() }) {
        Box(modifier = modifier.bindTopBarState()) {
            content.invoke()
        }
        topBar.invoke()
    }
}