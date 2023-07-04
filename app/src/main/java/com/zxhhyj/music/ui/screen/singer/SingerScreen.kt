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
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository
import com.zxhhyj.music.ui.common.AppTopBar
import com.zxhhyj.music.ui.common.bindAppTopBarState
import com.zxhhyj.music.ui.common.rememberAppTopBarState
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
    val appTopBarState = rememberAppTopBarState()
    LazyColumn(
        modifier = Modifier
            .bindAppTopBarState(appTopBarState)
            .fillMaxSize(),
        contentPadding = padding
    ) {
        items(AndroidMediaLibsRepository.artists) {
            ArtistItem(artist = it) {
                mainNavController.navigate(ScreenDestination.SingerCnt(it))
            }
        }
    }
    AppTopBar(
        state = appTopBarState,
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(id = R.string.singer)
    )
}