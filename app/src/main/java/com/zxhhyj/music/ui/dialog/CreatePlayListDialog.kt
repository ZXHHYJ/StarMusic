package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import com.zxhhyj.music.ui.common.AppDialog
import com.zxhhyj.music.ui.common.AppOutlinedTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreatePlayListDialog(onDismissRequest: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var title by rememberSaveable {
        mutableStateOf("")
    }
    AppDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.create_playlist),
        confirm = {
            TextButton(onClick = {
                if (title.isNotEmpty()) {
                    PlayListRepository.create(title)
                    onDismissRequest.invoke()
                }
            }) {
                Text(stringResource(id = R.string.create))
            }
        },
        dismiss = {
            Text(
                stringResource(id = R.string.cancel),
                modifier = Modifier.clickable { onDismissRequest.invoke() })
        }) {
        AppOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = title,
            onValueChange = {
                if (it.length <= 16) {
                    title = it
                }
            },
            placeholder = {
                Text(stringResource(id = R.string.title))
            })
        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            keyboardController?.show()
            onDispose {
                keyboardController?.hide()
            }
        }
    }
}