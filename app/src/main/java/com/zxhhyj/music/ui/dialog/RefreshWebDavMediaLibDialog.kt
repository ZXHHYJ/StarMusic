package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.ComposeToast
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.YesNoDialog
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import kotlinx.coroutines.delay

@Composable
fun RefreshWebDavMediaLibDialog(
    dialogNavController: NavController<DialogDestination>,
) {
    PlayerManager.clearPlayList()
    val context = LocalContext.current
    YesNoDialog(
        onDismissRequest = {
            dialogNavController.pop()
        },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = stringResource(id = R.string.refresh_webdav_media_lib),
        positive = {},
        negative = {})
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
        try {
            WebDavMediaLibRepository.scanMedia()
        } catch (_: Exception) {
            delay(100)
            //避免闪烁影响用户体验
            dialogNavController.pop()
            ComposeToast.postErrorToast(context.getString(R.string.link_error))
            return@LaunchedEffect
        }
        dialogNavController.pop()
    }
}