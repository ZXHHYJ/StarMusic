package studio.mandysa.music.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

@Composable
fun RoundIcon(
    size: Dp,
    imageVector: ImageVector,
    contentDescription: String?,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: () -> Unit
) {
    Icon(
        imageVector, contentDescription,
        Modifier
            .size(size)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(radius = size / 2),
                onClick = onClick
            ), tint
    )
}