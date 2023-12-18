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
import com.zxhhyj.music.logic.bean.PlayListBean
import com.zxhhyj.music.logic.repository.PlayListRepository.rename
import com.zxhhyj.ui.view.AppTextField
import com.zxhhyj.ui.view.YesNoDialog

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditPlayListTitleDialog(onDismissRequest: () -> Unit, playListBean: PlayListBean) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var titleValue by remember {
        mutableStateOf(TextFieldValue(playListBean.name, TextRange(playListBean.name.length)))
    }
    YesNoDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.edit_playlist_title),
        positive = {
            Text(
                stringResource(id = R.string.yes),
                modifier = Modifier.clickable {
                    if (titleValue.text.isNotEmpty()) {
                        playListBean.rename(titleValue.text)
                        onDismissRequest.invoke()
                    }
                })
        },
        negative = {
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