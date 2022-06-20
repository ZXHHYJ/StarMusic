package studio.mandysa.music.ui.screen.playlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.material.placeholder
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.R
import studio.mandysa.music.logic.ktx.playManager
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.cornerShape
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.textColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
    playlistModel: PlaylistModel = viewModel(factory = viewModelFactory {
        addInitializer(PlaylistModel::class) { return@addInitializer PlaylistModel(id) }
    })
) {
    val playlistInfo by playlistModel.playlistInfoModel.observeAsState()
    val songs = playlistModel.songs.collectAsLazyPagingItems()
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            elevation = 0.dp
        ) {
            IconButton(onClick = { mainNavController.pop() }) {
                Icon(Icons.Rounded.ArrowBack, null)
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Card(
                        modifier = Modifier.size(250.dp),
                        shape = cornerShape,
                        elevation = 10.dp
                    ) {
                        AsyncImage(
                            model = playlistInfo?.coverImgUrl ?: "",
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier
                            .defaultMinSize(minWidth = 100.dp)
                            .padding(horizontal = horizontalMargin)
                            .placeholder(playlistInfo == null),
                        text = playlistInfo?.name ?: "",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalMargin)
                            .clickable {
                                playlistInfo?.description?.let {
                                    dialogNavController.navigate(DialogDestination.Message(it))
                                }
                            }
                            .placeholder(playlistInfo == null),
                        text = playlistInfo?.description ?: "",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 5
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = horizontalMargin)
                        .padding(bottom = 5.dp)
                ) {
                    MenuItem(
                        modifier = Modifier.weight(1.0f),
                        title = stringResource(id = R.string.play_all),
                        imageVector = Icons.Rounded.PlayArrow,
                        enabled = songs.itemSnapshotList.items.isNotEmpty()
                    ) {
                        playManager {
                            loadPlaylist(songs.itemSnapshotList.items, 0)
                            play()
                        }
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    MenuItem(
                        modifier = Modifier.weight(1.0f),
                        title = stringResource(id = R.string.more),
                        imageVector = Icons.Rounded.MoreVert
                    ) {
                        dialogNavController.navigate(DialogDestination.PlaylistMenu(id))
                    }
                }
            }
            stickyHeader {
                Divider(thickness = 1.dp)
            }
            itemsIndexed(songs) { pos, _ ->
                SongItem(dialogNavController, songs[pos]!!) {
                    playManager {
                        loadPlaylist(songs.itemSnapshotList.items, pos)
                        play()
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}