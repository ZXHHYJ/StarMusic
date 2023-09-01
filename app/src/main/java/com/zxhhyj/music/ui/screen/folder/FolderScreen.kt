package com.zxhhyj.music.ui.screen.folder

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold

@Composable
fun FolderScreen(paddingValues: PaddingValues) {
    AppScaffold(topBar = {
        AppCenterTopBar(modifier = Modifier.fillMaxWidth(), title = "文件夹") {

        }
    }, modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .padding(paddingValues)) {

    }
}