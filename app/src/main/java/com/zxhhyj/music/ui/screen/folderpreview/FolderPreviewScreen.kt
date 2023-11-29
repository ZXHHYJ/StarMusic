package com.zxhhyj.music.ui.screen.folderpreview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.music.logic.bean.Folder
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn

@Composable
fun FolderPreviewScreen(
    paddingValues: PaddingValues,
    folder: Folder
) {
    AppScaffold(
        topBar = {
            AppCenterTopBar(title = { Text(text = folder.path.substringAfterLast("/")) })
        }, modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues)
    ) {
        RoundColumn(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(folder.songs) {
                    SongItem(songBean = it)
                }
            }
        }
    }
}