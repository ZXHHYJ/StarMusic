package com.zxhhyj.music.ui.screen.about

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.olshevski.navigation.reimagined.NavController
import com.zxhhyj.music.R
import com.zxhhyj.music.ui.composable.TopAppBar
import com.zxhhyj.music.ui.composable.bindTopAppBarState
import com.zxhhyj.music.ui.composable.rememberTopAppBarState
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.ScreenDestination


@Composable
fun AboutScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues,
) {
    val topAppBarState = rememberTopAppBarState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .bindTopAppBarState(topAppBarState)
    ) {

    }
    TopAppBar(
        state = topAppBarState, modifier = Modifier, title = stringResource(id = R.string.about)
    )
}