package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.verticalMargin

@Composable
fun MenuItem(title: String, imageVector: ImageVector, onClick: () -> Unit) {
    FilledTonalButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(horizontal = horizontalMargin),
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