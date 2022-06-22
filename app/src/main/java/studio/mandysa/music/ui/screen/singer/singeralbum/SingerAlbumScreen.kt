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
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.ui.item.AlbumItem
import studio.mandysa.music.ui.screen.ScreenDestination

@Composable
fun SingerAlbumScreen(
    mainNavController: NavController<ScreenDestination>,
    paddingValues: PaddingValues,
    id: String,
    singerAlbumViewModel: SingerAlbumViewModel = viewModel(factory = viewModelFactory {
        addInitializer(SingerAlbumViewModel::class) { SingerAlbumViewModel(id) }
    })
) {
    val albums = singerAlbumViewModel.albums.collectAsLazyPagingItems()
    LazyColumn {
        items(albums) {
            AlbumItem(mateAlbum = it!!) {
                mainNavController.navigate(ScreenDestination.Album(it.id))
            }
        }
        item {
            Spacer(modifier = Modifier.padding(paddingValues))
        }
    }
}