package studio.mandysa.music.ui.screen.singer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
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
import androidx.paging.compose.items
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppDivider
import studio.mandysa.music.ui.common.AppLazyVerticalGrid
import studio.mandysa.music.ui.item.AlbumItem
import studio.mandysa.music.ui.item.ContentColumnItem
import studio.mandysa.music.ui.item.ItemSubTitle
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.onBackground

@Composable
fun SingerScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
    singerViewModel: SingerViewModel = viewModel(factory = viewModelFactory {
        addInitializer(SingerViewModel::class) { SingerViewModel(id) }
    })
) {
    val singerInfo by singerViewModel.singerInfo.observeAsState()
    val albums = singerViewModel.albumSource.collectAsLazyPagingItems()
    val songs by singerViewModel.songs.observeAsState(listOf())
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
        AppLazyVerticalGrid {
            item {
                ContentColumnItem(
                    dialogNavController = dialogNavController,
                    coverUrl = singerInfo?.cover ?: "",
                    title = singerInfo?.name ?: "",
                    message = singerInfo?.briefDesc ?: ""
                ) {
                    //no toolbar
                }
            }
            item { AppDivider() }
            item {
                ItemSubTitle(stringResource(id = R.string.album))
            }
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = horizontalMargin),
                    horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2)
                ) {
                    items(albums) {
                        AlbumItem(mateAlbum = it!!) {
                            mainNavController.navigate(ScreenDestination.Album(it.id))
                        }
                    }
                }
            }
            item {
                ItemSubTitle(stringResource(id = R.string.popular_song))
            }
            autoItems(songs.size) {
                SongItem(dialogNavController = dialogNavController, song = songs[it]) {
                    PlayManager.loadPlaylist(songs, it)
                }
            }
            item {
                Spacer(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}