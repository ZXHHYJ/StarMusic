package com.zxhhyj.music.ui.screen.foldercnt

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.music.logic.bean.Folder
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.roundColumn
import dev.olshevski.navigation.reimagined.NavController

@Composable
fun FolderCntScreen(
    paddingValues: PaddingValues,
    sheetNavController: NavController<SheetDestination>,
    folder: Folder
) {
    AppScaffold(
        topBar = {
            AppCenterTopBar(title = { Text(text = folder.path.substringAfterLast("/")) })
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            roundColumn {
                itemsIndexed(folder.songs) { index, song ->
                    SongItem(songBean = song, sheetNavController = sheetNavController) {
                        PlayerManager.play(folder.songs, index)
                    }
                }
            }
        }
    }
}