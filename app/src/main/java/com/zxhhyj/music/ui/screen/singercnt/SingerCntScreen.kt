package com.zxhhyj.music.ui.screen.singercnt

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.olshevski.navigation.reimagined.NavController
import com.zxhhyj.music.logic.repository.LocalMediaRepository.songs
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.ui.common.BoxWithPercentages
import com.zxhhyj.music.ui.common.TopAppBar
import com.zxhhyj.music.ui.common.bindTopAppBarState
import com.zxhhyj.music.ui.common.rememberTopAppBarState
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.ScreenDestination



@Composable
fun SingerCntScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues,
    artist: SongBean.Local.Artist
) {
    BoxWithPercentages(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val topAppBarState = rememberTopAppBarState()
        LazyColumn(
            modifier = Modifier
                .bindTopAppBarState(topAppBarState)
                .fillMaxSize(),
            contentPadding = padding
        ) {
            itemsIndexed(artist.songs) { index, item ->
                SongItem(sheetNavController = sheetNavController, song = item) {
                    PlayManager.play(artist.songs, index)
                }
            }
        }
        TopAppBar(
            state = topAppBarState,
            modifier = Modifier.fillMaxWidth(),
            title = artist.name
        ) {

        }
    }
}
