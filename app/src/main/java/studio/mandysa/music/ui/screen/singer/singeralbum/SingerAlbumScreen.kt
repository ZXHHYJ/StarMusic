package studio.mandysa.music.ui.screen.singer.singeralbum

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination

@Composable
fun SingerAlbumScreen(
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
    singerAlbumViewModel: SingerAlbumViewModel = viewModel(factory = viewModelFactory {
        addInitializer(SingerAlbumViewModel::class) { SingerAlbumViewModel(id) }
    })
) {
    val songs = singerAlbumViewModel.albums.collectAsLazyPagingItems()
    LazyColumn {
        items(songs) {
            SongItem(dialogNavController = dialogNavController, song = it!!) {

            }
        }
        item {
            Spacer(modifier = Modifier.padding(paddingValues))
        }
    }
}