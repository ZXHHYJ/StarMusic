package com.zxhhyj.ui.view.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles
import com.zxhhyj.ui.theme.StarDimens
import com.zxhhyj.ui.view.AppSlider

@Composable
fun ItemSlider(
    text: @Composable () -> Unit,
    subText: @Composable () -> Unit,
    value: Float,
    onValueChange: (Float) -> Unit,
    onDragStart: () -> Unit = {},
    onDragEnd: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = StarDimens.horizontal, vertical = StarDimens.vertical),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyles.current.sub,
                LocalContentColor provides LocalColorScheme.current.subText
            ) {
                text()
                subText()
            }
        }
        Spacer(modifier = Modifier.height(StarDimens.vertical))
        AppSlider(
            value = value,
            onValueChange = onValueChange,
            onDragStart = onDragStart,
            onDragEnd = onDragEnd
        )
    }
}