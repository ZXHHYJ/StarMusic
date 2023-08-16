package com.zxhhyj.music.ui.screen.album

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository
import com.zxhhyj.music.ui.common.BoxWithPercentages
import com.zxhhyj.music.ui.item.AlbumItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.search.SearchScreenTabs
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate


@Composable
fun AlbumScreen(
    mainNavController: NavController<ScreenDestination>,
    paddingValues: PaddingValues
) {
    BoxWithPercentages(modifier = Modifier.fillMaxSize()) {
        var fixedCount = (maxWidth / 180.dp).toInt()
        if (fixedCount < 2) {
            fixedCount = 2
        }
        AppScaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalColorScheme.current.background)
                .statusBarsPadding()
                .padding(paddingValues),
            topBar = {
                AppCenterTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = R.string.album),
                    actions = {
                        AppIconButton(onClick = {
                            mainNavController.navigate(
                                ScreenDestination.Search(
                                    SearchScreenTabs.Album
                                )
                            )
                        }) {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                        }
                    }
                )
            }) {
            RoundColumn(modifier = Modifier.fillMaxSize()) {
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