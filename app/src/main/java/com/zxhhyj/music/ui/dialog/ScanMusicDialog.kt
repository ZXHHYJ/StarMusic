package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository
import com.zxhhyj.music.ui.common.AppDialog
import com.zxhhyj.music.ui.theme.appAccentColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScanMusicDialog(onDismissRequest: () -> Unit) {
    AppDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = stringResource(id = R.string.scan_music),
        confirm = {

        },
        dismiss = {

        })
    {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                strokeWidth = 4.dp,
                color = appAccentColor
            )
        }
    }
    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            delay(100)
            AndroidMediaLibsRepository.scanMedia()
            onDismissRequest.invoke()
        }
    }
}