package studio.mandysa.music.ui.screen.search.single

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.olshevski.navigation.reimagined.NavController
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
    /*val songs = searchSingleViewModel.songSource.collectAsLazyPagingItems()
    LazyColumn {
        itemsIndexed(songs) { index, item ->
            SongItem(dialogNavController = dialogNavController, song = item!!) {
                PlayManager.play(songs.itemSnapshotList, index)
            }
        }
        item {

        }
        item {
            Spacer(modifier = Modifier.padding(paddingValues))
        }
    }*/
}