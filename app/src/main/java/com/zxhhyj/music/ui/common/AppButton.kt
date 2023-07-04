package com.zxhhyj.music.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.ui.theme.appAccentColor
import com.zxhhyj.music.ui.theme.appTextButtonAccentColor
import com.zxhhyj.music.ui.theme.appUnEnabledColor
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.onTextColor
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

@Composable
fun AppMenuButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    text: String,
    enabled: Boolean = true
) {
    Surface(
        color = Color.Transparent,
        contentColor = if (enabled) appTextButtonAccentColor else appUnEnabledColor,
        shape = roundShape
    ) {
        Column(modifier = modifier.clickable(enabled) {
            onClick.invoke()
        }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = horizontal, vertical = vertical),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1.0f)
                )
                Icon(imageVector = imageVector, contentDescription = text)
            }
            AppDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontal)
            )
        }
    }
}

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    text: String
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = appAccentColor,
            contentColor = onTextColor
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        ),
        contentPadding = PaddingValues(horizontal = horizontal/2)
    ) {
        Icon(imageVector = imageVector, contentDescription = null)
        Text(text = text, fontSize = 14.sp)
    }
}