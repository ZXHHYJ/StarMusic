package studio.mandysa.music.ui.screen.album

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
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
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.item.ItemCoverHeader
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.onBackground

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
    albumViewModel: AlbumViewModel = viewModel(factory = viewModelFactory {
        addInitializer(AlbumViewModel::class) { AlbumViewModel(id) }
    })
) {
    val albumInfo by albumViewModel.albumInfo.observeAsState()
    Column {
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
        LazyColumn {
            item {
                ItemCoverHeader(
                    dialogNavController = dialogNavController,
                    coverUrl = albumInfo?.picUrl,
                    title = albumInfo?.name,
                    message = albumInfo?.description
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
                        enabled = albumInfo?.songList != null
                    ) {
                        PlayManager.loadPlaylist(albumInfo!!.songList, 0)
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    MenuItem(
                        modifier = Modifier.weight(1.0f),
                        title = stringResource(id = R.string.more),
                        imageVector = Icons.Rounded.MoreVert
                    ) {
                        // TODO: 专辑菜单
                    }
                }
            }
            stickyHeader {
                Divider(thickness = 1.dp)
            }
            albumInfo?.let {
                itemsIndexed(it.songList) { index, item ->
                    SongItem(dialogNavController = dialogNavController, song = item) {
                        PlayManager.loadPlaylist(it.songList, index)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}