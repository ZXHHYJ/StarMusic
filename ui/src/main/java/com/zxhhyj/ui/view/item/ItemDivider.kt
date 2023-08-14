package com.zxhhyj.ui.view.item

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.ui.theme.StarDimens
import com.zxhhyj.ui.view.AppDivider

@Composable
fun ItemDivider() {
    AppDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = StarDimens.horizontal)
    )
}