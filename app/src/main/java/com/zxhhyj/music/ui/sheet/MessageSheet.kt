package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.textColor

@Composable
fun MessageSheet(message: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = horizontal)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .verticalScroll(
                    state = rememberScrollState()
                ),
            text = message,
            color = textColor
        )
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}