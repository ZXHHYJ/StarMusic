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
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.YesNoDialog

@Composable
fun SyncWebDavMediaLibDialog(
    onDismissRequest: () -> Unit
) {
    YesNoDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = stringResource(id = R.string.sync_webdav),
        confirm = {},
        dismiss = {})
    {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                strokeWidth = 4.dp,
                color = LocalColorScheme.current.highlight
            )
        }
    }
    LaunchedEffect(Unit) {
        WebDavMediaLibRepository.scanMedia()
        onDismissRequest.invoke()
    }
}