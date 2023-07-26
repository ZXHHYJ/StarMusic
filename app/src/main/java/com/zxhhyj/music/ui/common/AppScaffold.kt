package com.zxhhyj.music.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier

val LocalTopBarState = compositionLocalOf { AppTopBarState() }

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalTopBarState provides rememberSaveable { AppTopBarState() }) {
        Box(modifier = modifier.bindAppTopBarState()) {
            content.invoke()
        }
        topBar.invoke()
    }
}