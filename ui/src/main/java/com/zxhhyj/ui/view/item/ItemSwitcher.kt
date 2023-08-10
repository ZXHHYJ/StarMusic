package com.zxhhyj.ui.view.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles
import com.zxhhyj.ui.theme.StarDimens
import com.zxhhyj.ui.view.Divider
import com.zxhhyj.ui.view.Switch

@Composable
fun ItemSwitcher(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    subText: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
) {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(StarDimens.round),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = StarDimens.horizontal, vertical = StarDimens.vertical),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompositionLocalProvider(LocalContentColor provides LocalColorScheme.current.highlight) {
                    icon()
                }
                Spacer(modifier = Modifier.width(StarDimens.vertical))
                Column(verticalArrangement = Arrangement.Center) {
                    CompositionLocalProvider(
                        LocalTextStyle provides LocalTextStyles.current.main,
                        LocalContentColor provides LocalColorScheme.current.text
                    ) {
                        text()
                    }
                    CompositionLocalProvider(
                        LocalTextStyle provides LocalTextStyles.current.sub,
                        LocalContentColor provides LocalColorScheme.current.subText
                    ) {
                        subText()
                    }
                }
                Spacer(modifier = Modifier.weight(1.0f))
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier.height(22.dp)
                )
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