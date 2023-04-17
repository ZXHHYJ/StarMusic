package com.zxhhyj.music.ui.screen.singer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.LocalMediaRepository
import com.zxhhyj.music.ui.common.MediaPermission
import com.zxhhyj.music.ui.common.TopAppBar
import com.zxhhyj.music.ui.common.bindTopAppBarState
import com.zxhhyj.music.ui.common.rememberTopAppBarState
import com.zxhhyj.music.ui.item.ArtistItem
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.ScreenDestination



@Composable
fun SingerScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues
) {
    MediaPermission(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val topAppBarState = rememberTopAppBarState()
        LazyColumn(
            modifier = Modifier.bindTopAppBarState(topAppBarState),
            contentPadding = padding
        ) {
            items(LocalMediaRepository.artists) {
                ArtistItem(artist = it) {
                    mainNavController.navigate(ScreenDestination.SingerCnt(it))
                }
            }
        }
        TopAppBar(
            state = topAppBarState,
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.singer)
        ) {

        }
    }
}