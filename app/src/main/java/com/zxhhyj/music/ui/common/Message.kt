package com.zxhhyj.music.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.olshevski.navigation.reimagined.NavController
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.textColor



@Composable
fun Message(sheetNavController: NavController<BottomSheetDestination>, message: String) {
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
        Spacer(modifier = Modifier.height(5.dp))
        /*MenuButton(
            title = stringResource(id = R.string.copy),
            imageVector = Icons.Rounded.ContentCopy
        ) {
            copyText(message)
            sheetNavController.pop()
        }*/
    }
}