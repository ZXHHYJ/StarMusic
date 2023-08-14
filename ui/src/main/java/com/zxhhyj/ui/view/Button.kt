package com.zxhhyj.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles
import com.zxhhyj.ui.theme.StarDimens
import com.zxhhyj.ui.utils.roundClickable

@Composable
fun AppListButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    text: String,
    textColor: Color = LocalColorScheme.current.highlight
) {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(StarDimens.round),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = StarDimens.horizontal, vertical = StarDimens.vertical),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompositionLocalProvider(LocalContentColor provides LocalColorScheme.current.highlight) {
                    Icon(imageVector = imageVector, contentDescription = text)
                }
                Spacer(modifier = Modifier.width(StarDimens.vertical))
                Column(verticalArrangement = Arrangement.Center) {
                    CompositionLocalProvider(
                        LocalTextStyle provides LocalTextStyles.current.main,
                        LocalContentColor provides LocalColorScheme.current.text
                    ) {
                        Text(text = text, color = textColor)
                    }
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = StarDimens.horizontal)
            )
        }
    }
}

@Composable
fun AppIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .roundClickable(enabled) {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha, content = content)
    }
}

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    text: String,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = LocalColorScheme.current.highlight,
            contentColor = LocalColorScheme.current.text
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        ),
        shape = RoundedCornerShape(StarDimens.round),
        contentPadding = PaddingValues(horizontal = StarDimens.horizontal / 2)
    ) {
        Icon(imageVector = imageVector, contentDescription = null, tint = Color.White)
        Text(text = text, style = LocalTextStyles.current.main, color = Color.White)
    }
}