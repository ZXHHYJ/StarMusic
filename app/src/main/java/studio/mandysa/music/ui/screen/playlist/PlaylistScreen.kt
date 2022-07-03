package studio.mandysa.music.ui.screen.playlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
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
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppDivider
import studio.mandysa.music.ui.common.AppLazyVerticalGrid
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.item.ContentColumnItem
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
    playlistViewModel: PlaylistViewModel = viewModel(factory = viewModelFactory {
        addInitializer(PlaylistViewModel::class) { return@addInitializer PlaylistViewModel(id) }
    })
) {
    val playlistInfo by playlistViewModel.playlistInfoModel.observeAsState()
    val songs by playlistViewModel.songsLiveData.observeAsState()
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
        AppLazyVerticalGrid(modifier = Modifier.fillMaxSize()) {
            item {
                ContentColumnItem(
                    dialogNavController = dialogNavController,
                    coverUrl = playlistInfo?.coverImgUrl,
                    title = playlistInfo?.name,
                    message = playlistInfo?.description
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = horizontalMargin)
                            .padding(bottom = 5.dp)
                    ) {
                        MenuItem(
                            modifier = Modifier.weight(1.0f),
                            title = stringResource(id = R.string.play_all),
                            imageVector = Icons.Rounded.PlayArrow,
                            enabled = songs?.isNotEmpty() ?: false
                        ) {
                            PlayManager.loadPlaylist(songs!!, 0)
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
            }
            item {
                AppDivider()
            }
            songs?.let {
                autoItems(it.size) { pos ->
                    SongItem(dialogNavController, it[pos]) {
                        PlayManager.loadPlaylist(it, pos)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}