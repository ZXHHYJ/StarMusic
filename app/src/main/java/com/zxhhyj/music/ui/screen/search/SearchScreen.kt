package com.zxhhyj.music.ui.screen.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.ui.common.TopAppBar
import com.zxhhyj.music.ui.common.rememberTopAppBarState
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import dev.olshevski.navigation.reimagined.NavController

@Composable
fun SearchScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues,
) {
    val topAppBarState = rememberTopAppBarState()
    TopAppBar(
        state = topAppBarState, modifier = Modifier, title = stringResource(id = R.string.search)
    )
}