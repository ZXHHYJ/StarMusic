package studio.mandysa.music.ui.screen.playlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppDivider
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.item.ItemCoverHeader
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.onBackground

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
    val songs = playlistModel.songSource.collectAsLazyPagingItems()
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            contentColor = onBackground,
            elevation = 0.dp
        ) {
            IconButton(onClick = { mainNavController.pop() }) {
                Icon(Icons.Rounded.ArrowBack, null)
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                ItemCoverHeader(
                    dialogNavController = dialogNavController,
                    coverUrl = playlistInfo?.coverImgUrl,
                    title = playlistInfo?.name,
                    message = playlistInfo?.description
                )
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
                        PlayManager.loadPlaylist(songs.itemSnapshotList.items, 0)
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
                AppDivider()
            }
            itemsIndexed(songs) { pos, _ ->
                SongItem(dialogNavController, songs[pos]!!) {
                    PlayManager.loadPlaylist(songs.itemSnapshotList.items, pos)
                }
            }
            item {
                Spacer(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}