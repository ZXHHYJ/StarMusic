package com.zxhhyj.music.ui.screen.hidesongs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.ui.common.topbar.TopAppBar
import com.zxhhyj.music.ui.common.topbar.bindTopAppBarState
import com.zxhhyj.music.ui.common.topbar.rememberTopAppBarState

@Composable
fun HiddenSongsScreen(padding: PaddingValues) {
    val topAppBarState = rememberTopAppBarState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .bindTopAppBarState(topAppBarState)
    ) {

    }
    TopAppBar(
        state = topAppBarState, modifier = Modifier, title = stringResource(id = R.string.hidden_songs)
    )
}