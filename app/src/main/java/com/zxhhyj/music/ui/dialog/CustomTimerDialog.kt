package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import com.zxhhyj.music.R
import com.zxhhyj.music.service.playermanager.PlayerTimerManager
import com.zxhhyj.ui.view.AppTextField
import com.zxhhyj.ui.view.YesNoDialog

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomTimerDialog(onDismissRequest: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var minutes by rememberSaveable {
        mutableStateOf<Int?>(null)
    }
    YesNoDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.custom_timer),
        confirm = {
            Text(text = stringResource(id = R.string.yes), modifier = Modifier.clickable {
                minutes?.let {
                    PlayerTimerManager.startCustomTimer(it * 60 * 1000)
                    onDismissRequest.invoke()
                }
            })
        },
        dismiss = {
            Text(
                text = stringResource(id = R.string.cancel),
                modifier = Modifier.clickable { onDismissRequest.invoke() })
        }) {
        AppTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = true,
            value = "${minutes ?: ""}",
            onValueChange = {
                minutes = it.toIntOrNull()
            },
            placeholder = {
                Text(text = stringResource(id = R.string.minute_number))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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