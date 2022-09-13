package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.theme.containerColor
import studio.mandysa.music.ui.theme.verticalMargin

@Composable
fun AppMenuButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .height(50.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(backgroundColor = containerColor),
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier.weight(1.0f),
            text = title, fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(imageVector = icon, contentDescription = null)
    }
    Spacer(modifier = Modifier.height(verticalMargin))
}