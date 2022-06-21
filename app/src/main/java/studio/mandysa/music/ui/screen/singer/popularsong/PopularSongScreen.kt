package studio.mandysa.music.ui.screen.singer.popularsong

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@Composable
fun PopularSongScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
    popularSongViewModel: PopularSongViewModel = viewModel(factory = viewModelFactory {
        addInitializer(PopularSongViewModel::class) { PopularSongViewModel(id) }
    })
) {
    val songs by popularSongViewModel.songs.observeAsState(listOf())
    LazyColumn() {
        items(songs) {
            SongItem(dialogNavController = dialogNavController, song = it) {

            }
        }
        item {
            Spacer(modifier = Modifier.padding(paddingValues))
        }
    }
}