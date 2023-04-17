package com.zxhhyj.music.ui.screen.setting

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
import com.zxhhyj.music.ui.common.AppDivider
import com.zxhhyj.music.ui.common.AppIcon
import com.zxhhyj.music.ui.common.AppSwitch
import com.zxhhyj.music.ui.theme.*

@Composable
fun SettingSwitchItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    title: String,
    subTitle: String,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
) {
    Surface(
        color = Color.Transparent,
        shape = roundShape
    ) {
        Column(modifier = modifier) {
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
                Spacer(modifier = Modifier.weight(1.0f))
                AppSwitch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier.height(24.dp)
                )
            }
            AppDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontal)
            )
        }
    }
}