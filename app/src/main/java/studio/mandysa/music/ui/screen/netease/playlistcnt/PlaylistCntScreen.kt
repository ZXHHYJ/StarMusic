package studio.mandysa.music.ui.screen.netease.playlistcnt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.common.Preview
import studio.mandysa.music.ui.item.ContentColumnItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin

@Composable
fun PlaylistCntScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
    playlistCntViewModel: PlaylistCntViewModel = viewModel(factory = viewModelFactory {
        addInitializer(PlaylistCntViewModel::class) { return@addInitializer PlaylistCntViewModel(id) }
    })
) {
    val playlistInfo by playlistCntViewModel.infoModelLiveData.observeAsState()
    val songs by playlistCntViewModel.songsLiveData.observeAsState()
    Preview(
        modifier = Modifier
            .statusBarsPadding(),
        refresh = { playlistCntViewModel.refresh() }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
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
                            enabled = songs?.isNotEmpty() == true
                        ) {
                            //PlayManager.play(songs!!, 0)
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        MenuItem(
                            modifier = Modifier.weight(1.0f),
                            title = stringResource(id = R.string.shuffle_play),
                            imageVector = Icons.Rounded.Shuffle,
                            enabled = songs?.isNotEmpty() == true
                        ) {
                            //PlayManager.shufflePlay(songs!!, 0)
                        }
                    }
                }
            }
            songs?.let { list ->
                items(list.size) { pos ->
                    /*SongItem(dialogNavController, list[pos]) {
                        PlayManager.play(list, pos)
                    }*/
                }
            }
        }
    }
}