package com.zxhhyj.music.ui.screen.singer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.media.MediaLibsRepository
import com.zxhhyj.music.ui.common.topbar.TopAppBar
import com.zxhhyj.music.ui.common.topbar.bindTopAppBarState
import com.zxhhyj.music.ui.common.topbar.rememberTopAppBarState
import com.zxhhyj.music.ui.item.ArtistItem
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun SingerScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues
) {
    val topAppBarState = rememberTopAppBarState()
    LazyColumn(
        modifier = Modifier
            .bindTopAppBarState(topAppBarState)
            .fillMaxSize(),
        contentPadding = padding
    ) {
        items(MediaLibsRepository.artists) {
            ArtistItem(artist = it) {
                mainNavController.navigate(ScreenDestination.SingerCnt(it))
            }
        }
    }
    TopAppBar(
        state = topAppBarState,
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(id = R.string.singer)
    )
}