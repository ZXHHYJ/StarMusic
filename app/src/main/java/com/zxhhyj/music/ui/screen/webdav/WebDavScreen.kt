package com.zxhhyj.music.ui.screen.webdav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import dev.olshevski.navigation.reimagined.NavController

@Composable
fun WebDavScreen(
    paddingValues: PaddingValues,
    sheetNavController: NavController<SheetDestination>,
    mainNavController: NavController<ScreenDestination>
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppCenterTopBar(
                modifier = Modifier, title = stringResource(id = R.string.webdav)
            )
        }) {

        RoundColumn(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(WebDavMediaLibRepository.davFileList) { it ->
                    SongItem(
                        sheetNavController = sheetNavController,
                        webDavFile = it,
                        onClick = {
                            WebDavMediaLibRepository.download(it)
                        },
                        downloadedOnClick = {
                            PlayManager.play(listOf(it), 0)
                        })
                }
            }
        }
    }
}