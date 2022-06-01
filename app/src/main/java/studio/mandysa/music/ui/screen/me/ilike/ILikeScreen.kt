package studio.mandysa.music.ui.screen.me.ilike

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
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.ui.item.SongItem

@Composable
fun ILikeScreen(
    navController: NavController<*>,
    viewModel: ILikeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val songs = viewModel.songs.collectAsLazyPagingItems()
    Column {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            elevation = 0.dp
        ) {
            IconButton(onClick = { navController.pop() }) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
        }
        LazyColumn {
            itemsIndexed(songs) { pos, model ->
                SongItem(
                    position = pos + 1,
                    title = model!!.title,
                    singer = model.artist.allArtist()
                ) {

                }
            }
        }
    }
}