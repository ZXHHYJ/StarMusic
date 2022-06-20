package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.theme.verticalMargin

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    title: String,
    imageVector: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    FilledTonalButton(
        modifier = modifier
            .height(50.dp), enabled = enabled,
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.weight(1.0f),
            text = title, fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(imageVector = imageVector, contentDescription = null)
    }
    Spacer(modifier = Modifier.height(verticalMargin))
}