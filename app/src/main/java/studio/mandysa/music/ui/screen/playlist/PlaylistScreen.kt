package studio.mandysa.music.ui.screen.playlist

import androidx.compose.foundation.layout.*
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
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppLazyVerticalGrid
import studio.mandysa.music.ui.common.AppMenuButton
import studio.mandysa.music.ui.common.Preview
import studio.mandysa.music.ui.item.ContentColumnItem
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin

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
    val playlistInfo by playlistViewModel.infoModelLiveData.observeAsState()
    val songs by playlistViewModel.songsLiveData.observeAsState()

    /*  val decayAnimationSpec = rememberSplineBasedDecay<Float>()
      val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
          decayAnimationSpec,
          rememberTopAppBarState()
      )
      AppScaffold(
          modifier = Modifier
              .statusBarsPadding()
              .nestedScroll(scrollBehavior.nestedScrollConnection),
          topBar = {
              AppMediumTopAppBar(
                  title = {},
                  navigationIcon = {
                      IconButton(onClick = { mainNavController.pop() }) {
                          Icon(Icons.Rounded.ArrowBack, null)
                      }
                  },
                  scrollBehavior = scrollBehavior
              )
          }) {*/
    Preview(modifier = Modifier, refresh = { playlistViewModel.refresh() }) {
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
                        AppMenuButton(
                            modifier = Modifier.weight(1.0f),
                            title = stringResource(id = R.string.play_all),
                            icon = Icons.Rounded.PlayArrow,
                            enabled = songs?.isNotEmpty() ?: false
                        ) {
                            PlayManager.play(songs!!, 0)
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        AppMenuButton(
                            modifier = Modifier.weight(1.0f),
                            title = stringResource(id = R.string.shuffle_play),
                            icon = Icons.Rounded.Shuffle,
                            enabled = songs?.isNotEmpty() ?: false
                        ) {
                            PlayManager.shufflePlay(songs!!, 0)
                        }
                    }
                }
            }
            songs?.let { list ->
                adaptiveItems(list.size) { pos ->
                    SongItem(dialogNavController, list[pos]) {
                        PlayManager.play(list, pos)
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}