package studio.mandysa.music.ui.screen.playlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.AsyncImage
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.theme.round

@Composable
fun PlaylistScreen(
    navController: NavHostController,
    id: String,
    playlist: PlaylistModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PlaylistModel(id) as T
        }
    })
) {
    playlist.refresh()
    val playlistInfo by playlist.getPlaylistInfo().observeAsState()
    val songs = playlist.songs.collectAsLazyPagingItems()
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            elevation = 0.dp
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBack, null)
            }
            Spacer(modifier = Modifier.weight(1.0f))
            IconButton(onClick = { }) {
                Icon(Icons.Rounded.MoreVert, null)
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            playlistInfo?.let {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Card(
                            modifier = Modifier.size(250.dp),
                            shape = RoundedCornerShape(round),
                            elevation = 3.dp
                        ) {
                            AsyncImage(
                                model = it.coverImgUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = it.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = it.description,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
            itemsIndexed(songs) { pos, model ->
                SongItem(
                    position = pos + 1,
                    title = model!!.title,
                    singer = model.artist.allArtist()
                ) {
                    PlayManager.loadPlaylist(songs.itemSnapshotList.items, pos)
                    PlayManager.play()
                }
            }
        }
    }

}