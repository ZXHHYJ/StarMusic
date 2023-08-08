package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.PlayListModel
import com.zxhhyj.music.ui.common.AppTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditPlayListTitleDialog(onDismissRequest: () -> Unit, model: PlayListModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var titleValue by remember {
        mutableStateOf(TextFieldValue(model.name, TextRange(model.name.length)))
    }
    com.zxhhyj.ui.YesNoDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.edit_playlist_title),
        confirm = {
            Text(
                stringResource(id = R.string.yes),
                modifier = Modifier.clickable {
                    if (titleValue.text.isNotEmpty()) {
                        model.rename(titleValue.text)
                        onDismissRequest.invoke()
                    }
                })
        },
        dismiss = {
            Text(
                stringResource(id = R.string.cancel),
                modifier = Modifier.clickable { onDismissRequest.invoke() })
        }) {
        AppTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = true,
            value = titleValue,
            onValueChange = {
                if (it.text.length <= 16) {
                    titleValue = it
                }
            },
            placeholder = {
                Text(text = stringResource(id = R.string.new_title))
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