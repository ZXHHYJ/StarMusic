package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.music.ui.common.AppDialog

@Composable
fun ScanMusicDialog(onDismissRequest: () -> Unit) {
    AppDialog(
        onDismissRequest = onDismissRequest,
        title = "扫描音乐",
        confirm = {
            Text(text = "确定", modifier = Modifier.clickable {
                onDismissRequest.invoke()
            })
        },
        dismiss = {
            Text(text = "取消", modifier = Modifier.clickable {
                onDismissRequest.invoke()
            })
        })
    {
        Text(text = "我是测试内容")
    }
}