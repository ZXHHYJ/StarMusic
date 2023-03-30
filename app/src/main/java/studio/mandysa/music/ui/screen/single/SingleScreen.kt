package studio.mandysa.music.ui.screen.single

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.logic.repository.LocalMediaRepository
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.MediaPermission
import studio.mandysa.music.ui.common.TopAppBar
import studio.mandysa.music.ui.common.bindTopAppBarState
import studio.mandysa.music.ui.common.rememberTopAppBarState
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@Composable
fun SingleScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues
) {
    MediaPermission(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val localBeans = LocalMediaRepository.getSongs()
        val topAppBarState = rememberTopAppBarState()
        LazyColumn(
            modifier = Modifier.bindTopAppBarState(topAppBarState),
            contentPadding = padding
        ) {
            itemsIndexed(localBeans) { index, item ->
                SongItem(dialogNavController = dialogNavController, song = item) {
                    PlayManager.play(localBeans, index)
                }
            }
        }
        TopAppBar(
            state = topAppBarState,
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = studio.mandysa.music.R.string.media_lib)
        ) {

        }
    }
}
