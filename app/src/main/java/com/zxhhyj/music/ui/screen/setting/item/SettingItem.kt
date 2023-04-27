package com.zxhhyj.music.ui.screen.setting.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.ui.common.AppDivider
import com.zxhhyj.music.ui.theme.appIconAccentColor
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.roundShape
import com.zxhhyj.music.ui.theme.textColor
import com.zxhhyj.music.ui.theme.vertical

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    title: String,
    subTitle: String? = null,
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
                Icon(
                    imageVector = imageVector,
                    contentDescription = title,
                    tint = appIconAccentColor
                )
                Spacer(modifier = Modifier.width(vertical))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, color = textColor)
                    subTitle?.let { Text(text = subTitle, fontSize = 14.sp, color = textColor) }
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