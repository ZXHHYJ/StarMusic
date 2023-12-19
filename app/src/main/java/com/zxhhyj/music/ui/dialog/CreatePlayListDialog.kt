package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.PlayListRepository
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.ui.view.AppTextField
import com.zxhhyj.ui.view.YesNoDialog
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreatePlayListDialog(dialogNavController: NavController<DialogDestination>) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var title by rememberSaveable {
        mutableStateOf("")
    }
    YesNoDialog(
        onDismissRequest = { dialogNavController.pop() },
        title = stringResource(id = R.string.create_playlist),
        positive = {
            Text(text = stringResource(id = R.string.create), modifier = Modifier.clickable {
                if (title.isNotEmpty()) {
                    PlayListRepository.create(title)
                    dialogNavController.pop()
                }
            })
        },
        negative = {
            Text(
                text = stringResource(id = R.string.cancel),
                modifier = Modifier.clickable { dialogNavController.pop() })
        }) {
        AppTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = true,
            value = title,
            onValueChange = {
                if (it.length <= 16) {
                    title = it
                }
            },
            placeholder = {
                Text(stringResource(id = R.string.title))
            }
        )
        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            keyboardController?.show()
            onDispose {
                keyboardController?.hide()
            }
        }
    }
}