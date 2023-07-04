package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.PlayListRepository
import com.zxhhyj.music.ui.common.AppDialog
import com.zxhhyj.music.ui.common.AppOutlinedTextField

@Composable
fun AddPlayListDialog(onDismissRequest: () -> Unit) {
    var playlistName by remember {
        mutableStateOf("")
    }
    AppDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.add_playlist),
        confirm = {
            Text(
                stringResource(id = R.string.create),
                modifier = Modifier.clickable {
                    if (playlistName.isNotEmpty()) {
                        PlayListRepository.create(playlistName)
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
            modifier = Modifier.fillMaxWidth(),
            value = playlistName,
            onValueChange = {
                if (it.length <= 16) {
                    playlistName = it
                }
            })
    }
}