package com.zxhhyj.music.ui.common.icon

import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.zxhhyj.music.ui.common.card.AppRoundCard

@Composable
fun AppRoundIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    AppRoundCard(backgroundColor = Color.Transparent) {
        Icon(
            imageVector,
            contentDescription,
            modifier,
            tint
        )
    }
}