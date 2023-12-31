package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.YesNoDialog
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop

@Composable
fun MediaLibsEmptyDialog(
    dialogNavController: NavController<DialogDestination>,
    mainNavController: NavController<ScreenDestination>,
) {
    YesNoDialog(
        onDismissRequest = {
            dialogNavController.pop()
        },
        title = stringResource(id = R.string.scan_music),
        positive = {
            Text(text = stringResource(id = R.string.yes), modifier = Modifier.clickable {
                dialogNavController.pop()
                mainNavController.navigate(ScreenDestination.MediaLibConfig)
            })
        },
        negative = {
            Text(text = stringResource(id = R.string.cancel), modifier = Modifier.clickable {
                dialogNavController.pop()
            })
        })
    {
        Text(
            text = stringResource(id = R.string.scan_music_dialog_text),
            color = LocalColorScheme.current.text
        )
    }
}