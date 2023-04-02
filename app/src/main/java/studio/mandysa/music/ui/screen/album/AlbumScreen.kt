package studio.mandysa.music.ui.screen.album

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
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.LocalMediaRepository
import studio.mandysa.music.ui.common.BoxWithPercentages
import studio.mandysa.music.ui.common.TopAppBar
import studio.mandysa.music.ui.common.bindTopAppBarState
import studio.mandysa.music.ui.common.rememberTopAppBarState
import studio.mandysa.music.ui.item.AlbumItem
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontal
import studio.mandysa.music.ui.theme.vertical

/**
 * @author 黄浩
 */

@Composable
fun AlbumScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues
) {
    BoxWithPercentages(modifier = Modifier.fillMaxSize()) {
        var fixedCount = (maxWidth / 180.dp).toInt()
        if (fixedCount < 2) {
            fixedCount = 2;
        }
        val topAppBarState = rememberTopAppBarState()
        LazyVerticalGrid(
            modifier = Modifier
                .bindTopAppBarState(topAppBarState)
                .fillMaxSize(),
            columns = GridCells.Fixed(fixedCount), contentPadding = padding
        ) {
            itemsIndexed(LocalMediaRepository.albums) { index, item ->
                val column = index % fixedCount
                val left = if (column == 0) horizontal else horizontal / 2
                val right = if (column < fixedCount - 1) horizontal / 2 else horizontal
                val paddingValues = PaddingValues(
                    top = vertical / 2,
                    bottom = vertical / 2,
                    start = left,
                    end = right
                )
                AlbumItem(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues), album = item
                ) {
                    mainNavController.navigate(ScreenDestination.AlbumCnt(item))
                }
            }
        }
        TopAppBar(
            state = topAppBarState,
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.album)
        )
    }
}