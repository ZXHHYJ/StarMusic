package com.zxhhyj.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles
import com.zxhhyj.ui.theme.StarDimens

@Composable
fun AppTabRow(
    modifier: Modifier = Modifier,
    tabs: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = StarDimens.horizontal,
                vertical = StarDimens.vertical
            )
    ) {
        tabs()
    }
}

@Composable
fun AppTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable (ColumnScope.() -> Unit)
) {
    AppCard(
        backgroundColor = if (selected) LocalColorScheme.current.highlight else Color.Transparent,
        shape = RoundedCornerShape(50)
    ) {
        Column(
            modifier
                .heightIn(min = 28.dp)
                .widthIn(min = 48.dp)
                .clickable { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CompositionLocalProvider(LocalContentColor provides if (enabled) (if (selected) LocalColorScheme.current.onText else LocalColorScheme.current.text) else LocalColorScheme.current.disabled,
                LocalTextStyle provides LocalTextStyles.current.sub) {
                content()
            }
        }
    }

}