package com.zxhhyj.ui.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles
import com.zxhhyj.ui.theme.StarDimens

@Composable
fun ListButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    text: String,
    textColor: Color = LocalColorScheme.current.highlight
) {
    Item(
        icon = { Icon(imageVector = imageVector, contentDescription = text) },
        text = { Text(text = text, color = textColor) },
        subText = { }) {
        onClick.invoke()
    }
}

@Composable
fun Button(
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