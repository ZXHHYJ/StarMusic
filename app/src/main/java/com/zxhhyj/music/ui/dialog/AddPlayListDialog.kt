package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.PlayListRepository
import com.zxhhyj.music.ui.common.AppDialog

@Composable
fun AddPlayListDialog(onDismissRequest: () -> Unit) {
    AppDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.add_playlist),
        confirm = {
            Text(
                stringResource(id = R.string.create),
                modifier = Modifier.clickable {
                    PlayListRepository.create("测试歌单")
                    onDismissRequest.invoke()
                })
        },
        dismiss = {
            Text(
                stringResource(id = R.string.cancel),
                modifier = Modifier.clickable { onDismissRequest.invoke() })
        }) {
        Text(text = "我是内容")
    }
}