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
import com.zxhhyj.music.logic.bean.PlayListModel
import com.zxhhyj.music.ui.common.AppDialog
import com.zxhhyj.music.ui.common.AppOutlinedTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditPlayListTitleDialog(onDismissRequest: () -> Unit, model: PlayListModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var title by rememberSaveable {
        mutableStateOf(model.name)
    }
    AppDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.edit_playlist_title),
        confirm = {
            Text(
                stringResource(id = R.string.yes),
                modifier = Modifier.clickable {
                    if (title.isNotEmpty()) {
                        model.rename(title)
                        onDismissRequest.invoke()
                    }
                })
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
                Text(text = stringResource(id = R.string.new_title))
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