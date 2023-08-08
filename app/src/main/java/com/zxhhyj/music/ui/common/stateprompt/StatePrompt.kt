package com.zxhhyj.music.ui.common.stateprompt

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.zxhhyj.ui.view.LocalTopBarState

@Composable
fun StatePrompt(
    modifier: Modifier = Modifier,
    empty: Boolean,
    content: @Composable () -> Unit
) {
    val barHeight = with(LocalDensity.current) {
        LocalTopBarState.current.barSize.height.toDp()
    }
    BoxWithConstraints(modifier) {
        when (empty) {
            true -> {
                EmptyContentMessage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(maxHeight - barHeight)
                )
            }

            false -> {
                content.invoke()
            }
        }
    }
}
