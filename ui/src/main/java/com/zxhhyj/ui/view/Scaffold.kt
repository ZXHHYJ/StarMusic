package com.zxhhyj.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier

val LocalTopBarState = compositionLocalOf { TopBarState() }

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalTopBarState provides rememberSaveable { TopBarState() }) {
        Box(modifier = modifier) {
            Box(
                modifier = Modifier
                    .bindTopBarState()
                    .fillMaxSize()
            ) {
                content.invoke()
            }
            topBar.invoke()
        }
    }
}