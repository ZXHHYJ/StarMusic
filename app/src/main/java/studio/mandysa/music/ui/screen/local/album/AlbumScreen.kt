package studio.mandysa.music.ui.screen.local.album

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.logic.repository.LocalMediaRepository
import studio.mandysa.music.ui.common.AppTopSearchBar
import studio.mandysa.music.ui.common.MediaPermission
import studio.mandysa.music.ui.item.AlbumItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
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
            val albums = LocalMediaRepository.getAlbums()
            LazyColumn(
                modifier = Modifier.weight(1.0f),
                contentPadding = padding
            ) {
                itemsIndexed(albums) { _, item ->
                    AlbumItem(album = item) {
                       mainNavController.navigate(ScreenDestination.AlbumCnt(item))
                    }
                }
            }
        }
    }
}