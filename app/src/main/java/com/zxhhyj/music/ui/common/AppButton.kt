package com.zxhhyj.music.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.zxhhyj.music.ui.theme.appButtonAccentColor
import com.zxhhyj.music.ui.theme.appTextButtonAccentColor
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.onTextColor
import com.zxhhyj.music.ui.theme.roundShape
import com.zxhhyj.music.ui.theme.vertical


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
            disabledElevation = 0.dp
        ),
        shape = roundShape,
        border = null,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = appButtonAccentColor,
            contentColor = onTextColor
        ),
        contentPadding = contentPadding,
        content = content
    )
}

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
                AppIcon(imageVector = imageVector, contentDescription = text)
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
                    .height(50.dp)
                    .padding(horizontal = horizontal, vertical = vertical),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = text)
                Spacer(modifier = Modifier.weight(1.0f))
                AppIcon(imageVector = imageVector, contentDescription = text)
            }
            AppDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontal)
            )
        }
    }
}