package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import studio.mandysa.music.ui.theme.appButtonBackgroundColor
import studio.mandysa.music.ui.theme.defaultRoundShape
import studio.mandysa.music.ui.theme.onTextColor

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable (RowScope.() -> Unit)
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        ),
        shape = defaultRoundShape,
        border = null,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = appButtonBackgroundColor,
            contentColor = onTextColor
        ),
        contentPadding = contentPadding,
        content = content
    )
}