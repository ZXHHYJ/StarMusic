package com.zxhhyj.ui.view.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles
import com.zxhhyj.ui.theme.StarDimens

@Composable
fun ItemSubTitle(title: @Composable () -> Unit){
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = StarDimens.horizontal)
            .padding(horizontal = StarDimens.horizontal, vertical = StarDimens.vertical)
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyles.current.sub,
            LocalContentColor provides LocalColorScheme.current.subText
        ) {
            title()
        }
    }
}