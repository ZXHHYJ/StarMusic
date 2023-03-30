package studio.mandysa.music.ui.screen.singer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.logic.repository.LocalMediaRepository
import studio.mandysa.music.ui.common.MediaPermission
import studio.mandysa.music.ui.item.ArtistItem
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@Composable
fun SingerScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues
) {
    MediaPermission(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            val artists = LocalMediaRepository.getArtists()
            LazyColumn(
                modifier = Modifier.weight(1.0f),
                contentPadding = padding
            ) {
                items(artists) {
                    ArtistItem(artist = it) {
                        mainNavController.navigate(ScreenDestination.SingerCnt(it))
                    }
                }
            }
        }
    }
}