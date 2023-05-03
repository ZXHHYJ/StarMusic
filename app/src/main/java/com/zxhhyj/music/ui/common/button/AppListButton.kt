package com.zxhhyj.music.ui.common.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.zxhhyj.music.ui.common.AppDivider
import com.zxhhyj.music.ui.common.AppRoundIcon
import com.zxhhyj.music.ui.theme.appTextButtonAccentColor
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.roundShape
import com.zxhhyj.music.ui.theme.vertical

@Composable
fun AppListButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    text: String
) {
    Surface(
        color = Color.Transparent,
        contentColor = appTextButtonAccentColor,
        shape = roundShape
    ) {
        Column(modifier = modifier.clickable {
            onClick.invoke()
        }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontal, vertical = vertical)
            ) {
                AppRoundIcon(imageVector = imageVector, contentDescription = text)
                Spacer(modifier = Modifier.width(vertical))
                Text(text = text)
            }
            AppDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontal)
            )
        }
    }
}