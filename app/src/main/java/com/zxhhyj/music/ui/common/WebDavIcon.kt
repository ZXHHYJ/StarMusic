package com.zxhhyj.music.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R

@Composable
fun WebDavIcon(modifier: Modifier = Modifier) {
    val fontSize = 8.sp
    Card(
        shape = RoundedCornerShape(2.dp),
        elevation = 0.dp,
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.webdav),
            modifier = modifier
                .background(Color.LightGray.copy(0.3f))
                .padding(horizontal = 2.dp),
            fontSize = fontSize
        )
    }
}