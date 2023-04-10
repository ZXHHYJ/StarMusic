package studio.mandysa.music.ui.screen.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.ui.composable.AppDivider
import studio.mandysa.music.ui.composable.AppIcon
import studio.mandysa.music.ui.theme.*

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    title: String,
    subTitle: String,
    onClick: () -> Unit,
) {
    Surface(
        color = Color.Transparent,
        shape = roundShape
    ) {
        Column(modifier = modifier.clickable {
            onClick.invoke()
        }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontal, vertical = vertical),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppIcon(
                    imageVector = imageVector,
                    contentDescription = title,
                    modifier = Modifier.size(30.dp),
                    tint = appIconAccentColor
                )
                Spacer(modifier = Modifier.width(vertical))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold)
                    Text(text = subTitle, fontSize = 14.sp)
                }
            }
            AppDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontal)
            )
        }
    }
}