package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.theme.vertical

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    title: String,
    imageVector: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    AppButton(
        onClick = onClick,
        modifier = modifier
            .height(50.dp),
        enabled = enabled,
    ) {
        Text(
            modifier = Modifier.weight(1.0f),
            text = title,
            fontSize = 17.sp,
            maxLines = 1,
            fontWeight = FontWeight.Bold
        )
        AppIcon(imageVector = imageVector, contentDescription = null)
    }
    Spacer(modifier = Modifier.height(vertical))
}