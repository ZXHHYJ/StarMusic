package com.zxhhyj.music.ui.screen.albumcnt

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.music.logic.repository.media.MediaLibsRepository.songs
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.ui.common.icon.AppRoundIcon
import com.zxhhyj.music.ui.common.topbar.TopAppBar
import com.zxhhyj.music.ui.common.topbar.bindTopAppBarState
import com.zxhhyj.music.ui.common.topbar.rememberTopAppBarState
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import dev.olshevski.navigation.reimagined.NavController


@Composable
fun AlbumCntScreen(
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues,
    album: SongBean.Album
) {
    val topAppBarState = rememberTopAppBarState()
    LazyColumn(
        modifier = Modifier
            .bindTopAppBarState(topAppBarState)
            .fillMaxSize(),
        contentPadding = padding
    ) {
        itemsIndexed(album.songs) { index, item ->
            SongItem(song = item, sheetNavController = sheetNavController) {
                PlayManager.play(album.songs, index)
            }
        }
    }
    TopAppBar(
        state = topAppBarState,
        modifier = Modifier.fillMaxWidth(),
        title = album.name
    )
    {
        AppRoundIcon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
    }
}