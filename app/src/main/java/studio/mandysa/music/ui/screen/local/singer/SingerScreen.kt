package studio.mandysa.music.ui.screen.local.singer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.logic.repository.LocalMediaRepository
import studio.mandysa.music.ui.common.AppTopSearchBar
import studio.mandysa.music.ui.common.MediaPermission
import studio.mandysa.music.ui.item.ArtistItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SingerScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    drawerState: DrawerState,
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
            AppTopSearchBar(drawerState = drawerState) {
                mainNavController.navigate(ScreenDestination.Search)
            }
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