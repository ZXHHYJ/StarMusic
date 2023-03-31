package studio.mandysa.music.ui.screen.albumcnt

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@Composable
fun AlbumCntScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetController: NavController<BottomSheetDestination>,
    paddingValues: PaddingValues,
    album: SongBean.Local.Album
) {

}