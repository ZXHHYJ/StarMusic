package studio.mandysa.music.ui.screen.singercnt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.logic.repository.LocalMediaRepository.songs
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.ui.common.BoxWithPercentages
import studio.mandysa.music.ui.common.TopAppBar
import studio.mandysa.music.ui.common.bindTopAppBarState
import studio.mandysa.music.ui.common.rememberTopAppBarState
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@Composable
fun SingerCntScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues,
    artist: SongBean.Local.Artist
) {
    BoxWithPercentages(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(padding)
    ) {
        val topAppBarState = rememberTopAppBarState()
        LazyColumn(
            modifier = Modifier
                .bindTopAppBarState(topAppBarState)
                .fillMaxSize()
        ) {
            itemsIndexed(artist.songs) { index, item ->
                SongItem(sheetNavController = sheetNavController, song = item) {
                    PlayManager.play(artist.songs, index)
                }
            }
        }
        TopAppBar(
            state = topAppBarState,
            modifier = Modifier.fillMaxWidth(),
            title = artist.name
        ) {

        }
    }
}
