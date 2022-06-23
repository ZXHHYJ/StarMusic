package studio.mandysa.music.ui.screen.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@Composable
fun SearchSongScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    keywords: String,
    searchSongViewModel: SearchSongViewModel = viewModel(factory = viewModelFactory {
        addInitializer(SearchSongViewModel::class) { SearchSongViewModel(keywords) }
    })
) {
    val songs = searchSongViewModel.songSource.collectAsLazyPagingItems()
    LazyColumn {
        itemsIndexed(songs) { index, item ->
            SongItem(dialogNavController = dialogNavController, song = item!!) {
                PlayManager.loadPlaylist(songs.itemSnapshotList, index)
            }
        }
        item {
            Spacer(modifier = Modifier.padding(paddingValues))
        }
    }
}