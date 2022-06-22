package studio.mandysa.music.ui.screen.singer.popularsong

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination

@Composable
fun PopularSongScreen(
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
    popularSongViewModel: PopularSongViewModel = viewModel(factory = viewModelFactory {
        addInitializer(PopularSongViewModel::class) { PopularSongViewModel(id) }
    })
) {
    val songs by popularSongViewModel.songs.observeAsState(listOf())
    LazyColumn {
        itemsIndexed(songs) { index, item ->
            SongItem(dialogNavController = dialogNavController, song = item) {
                PlayManager.loadPlaylist(songs, index)
            }
        }
        item {
            Spacer(modifier = Modifier.padding(paddingValues))
        }
    }
}