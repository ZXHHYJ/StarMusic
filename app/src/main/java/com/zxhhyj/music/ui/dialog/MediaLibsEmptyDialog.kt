package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.YesNoDialog
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun MediaLibsEmptyDialog(
    onDismissRequest: () -> Unit,
    mainNavController: NavController<ScreenDestination>
) {
    YesNoDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.scan_music),
        confirm = {
            Text(text = stringResource(id = R.string.yes), modifier = Modifier.clickable {
                onDismissRequest.invoke()
                mainNavController.navigate(ScreenDestination.MediaLibs)
            })
        },
        dismiss = {
            Text(text = stringResource(id = R.string.cancel), modifier = Modifier.clickable {
                onDismissRequest.invoke()
            })
        })
    {
        Text(
            text = stringResource(id = R.string.scan_music_dialog_text),
            color = LocalColorScheme.current.text
        )
    }
}