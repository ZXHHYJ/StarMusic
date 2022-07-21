package studio.mandysa.music.ui.screen.search.single

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

@Composable
fun SearchSingleScreen(
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    keywords: String,
    searchSingleViewModel: SearchSingleViewModel = viewModel(factory = viewModelFactory {
        addInitializer(SearchSingleViewModel::class) { SearchSingleViewModel(keywords) }
    })
) {
    val songs = searchSingleViewModel.songSource.collectAsLazyPagingItems()
    LazyColumn {
        itemsIndexed(songs) { index, item ->
            SongItem(dialogNavController = dialogNavController, song = item!!) {
                PlayManager.play(songs.itemSnapshotList, index)
            }
        }
        item {
            Spacer(modifier = Modifier.padding(paddingValues))
        }
    }
}