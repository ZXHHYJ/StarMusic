package studio.mandysa.music.ui.screen.me.like

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.item.SongItem

@Composable
fun LikeScreen(
    navController: NavController,
    id: String,
    likeViewModel: LikeViewModel = viewModel(factory = viewModelFactory {
        addInitializer(LikeViewModel::class) { return@addInitializer LikeViewModel(id) }
    })
) {
    val songs = likeViewModel.songs.collectAsLazyPagingItems()
    Column {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            elevation = 0.dp
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
        }
        LazyColumn {
            itemsIndexed(songs) { pos, _ ->
                SongItem(songs[pos]!!) {
                    PlayManager.apply {
                        loadPlaylist(songs.itemSnapshotList, pos)
                        play()
                    }
                }
            }
        }
    }
}