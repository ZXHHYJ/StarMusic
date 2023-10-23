package com.zxhhyj.ui.theme

import android.os.Build
import android.view.RoundedCorner
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

object StarDimens {
    /**
     * 常用垂直面边距
     */
    val vertical = 10.dp

    /**
     * 常用水平边距
     */
    val horizontal = 16.dp

    private val android12ScreenRound
        @Composable get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val cornerRadius =
                LocalView.current.rootWindowInsets?.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_RIGHT)?.radius
            cornerRadius?.let {
                with(LocalDensity.current) { it.toDp() }
            } ?: 8.dp
        } else {
            8.dp
        }

    /**
     * 常用圆角大小
     */
    val round
        @Composable get() = android12ScreenRound
}