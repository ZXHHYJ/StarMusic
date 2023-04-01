package studio.mandysa.music.ui.screen.album

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.LocalMediaRepository
import studio.mandysa.music.ui.common.MediaPermission
import studio.mandysa.music.ui.common.TopAppBar
import studio.mandysa.music.ui.common.bindTopAppBarState
import studio.mandysa.music.ui.common.rememberTopAppBarState
import studio.mandysa.music.ui.item.AlbumItem
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@Composable
fun AlbumScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues
) {
    MediaPermission(modifier = Modifier.fillMaxSize()) {
        val topAppBarState = rememberTopAppBarState()
        LazyColumn(
            modifier = Modifier.bindTopAppBarState(topAppBarState),
            contentPadding = padding
        ) {
            itemsIndexed(LocalMediaRepository.albums) { _, item ->
                AlbumItem(album = item) {
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