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
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.view.AppTextField
import com.zxhhyj.ui.view.YesNoDialog

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditWebDavPasswordDialog(onDismissRequest: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var password by remember {
        mutableStateOf(SettingRepository.WebDavConfig.password)
    }
    YesNoDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.webdav_password),
        confirm = {
            Text(
                stringResource(id = R.string.yes),
                modifier = Modifier.clickable {
                    SettingRepository.WebDavConfig =
                        SettingRepository.WebDavConfig.copy(password = password)
                    onDismissRequest.invoke()
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
            value = password,
            onValueChange = {
                password = it
            },
            placeholder = {
                Text(text = stringResource(id = R.string.webdav_password))
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