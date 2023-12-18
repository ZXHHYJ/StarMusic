package com.zxhhyj.music.ui.screen.folderpreview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.music.logic.bean.Folder
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.roundColumn

@Composable
fun FolderPreviewScreen(
    paddingValues: PaddingValues,
    folder: Folder
) {
    AppScaffold(
        topBar = {
            AppCenterTopBar(title = { Text(text = folder.path.substringAfterLast("/")) })
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            roundColumn {
                items(folder.songs) {
                    SongItem(songBean = it)
                }
            }

        }
    }
}