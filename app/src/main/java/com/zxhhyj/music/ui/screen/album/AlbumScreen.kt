package com.zxhhyj.music.ui.screen.album

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository
import com.zxhhyj.music.ui.common.AppScaffold
import com.zxhhyj.music.ui.common.AppTopBar
import com.zxhhyj.music.ui.common.BoxWithPercentages
import com.zxhhyj.music.ui.common.stateprompt.StatePrompt
import com.zxhhyj.music.ui.item.AlbumItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.vertical
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate


@Composable
fun AlbumScreen(
    mainNavController: NavController<ScreenDestination>,
    padding: PaddingValues
) {
    BoxWithPercentages(modifier = Modifier.fillMaxSize()) {
        var fixedCount = (maxWidth / 180.dp).toInt()
        if (fixedCount < 2) {
            fixedCount = 2
        }
        AppScaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            topBar = {
                AppTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = R.string.album)
                )
            }) {
            StatePrompt(
                empty = AndroidMediaLibsRepository.albums.isEmpty(),
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    columns = GridCells.Fixed(fixedCount),
                ) {
                    itemsIndexed(AndroidMediaLibsRepository.albums) { index, item ->
                        val column = index % fixedCount
                        val row: Int = index / fixedCount
                        val p = (2 * horizontal + (fixedCount - 1) * horizontal) * 1f / fixedCount
                        val left = horizontal + column * (horizontal - p)
                        val right = p - left
                        AlbumItem(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    PaddingValues.Absolute(
                                        top = if (row == 0) vertical else vertical / 2,
                                        left = left,
                                        right = right
                                    )
                                ), album = item
                        ) {
                            mainNavController.navigate(ScreenDestination.AlbumCnt(item))
                        }
                    }
                }
            }
        }
    }
}