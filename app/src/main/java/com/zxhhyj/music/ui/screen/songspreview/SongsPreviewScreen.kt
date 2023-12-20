package com.zxhhyj.music.ui.screen.songspreview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.roundColumn

@Composable
fun SongsPreviewScreen(
    paddingValues: PaddingValues,
    songs: List<SongBean>
) {
    AppScaffold(
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.songs_preview)) })
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            roundColumn {
                items(songs) {
                    SongItem(songBean = it)
                }
            }
        }
    }
}