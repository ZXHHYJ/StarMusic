package studio.mandysa.music.ui.screen.singer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppLazyVerticalGrid
import studio.mandysa.music.ui.common.Preview
import studio.mandysa.music.ui.item.ContentColumnItem
import studio.mandysa.music.ui.item.ItemSubTitle
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin

@Composable
fun SingerScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
    viewModel: SingerViewModel = viewModel(factory = viewModelFactory {
        addInitializer(SingerViewModel::class) { SingerViewModel(id) }
    })
) {
    val singerDetails = viewModel.singerDetails
    /*val albums = viewModel.albumSource.collectAsLazyPagingItems()*/
    val songs = viewModel.singerHotSongs
    Preview(modifier = Modifier, refresh = { viewModel.refresh() }) {
        AppLazyVerticalGrid() {
            item {
                ContentColumnItem(
                    dialogNavController = dialogNavController,
                    coverUrl = singerDetails?.cover ?: "",
                    title = singerDetails?.name ?: "",
                    message = singerDetails?.briefDesc ?: ""
                ) {
                    //no toolbar
                }
            }
            item {
                ItemSubTitle(stringResource(id = R.string.album))
            }
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = horizontalMargin),
                    horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2)
                ) {
                    /*items(albums) {
                        AlbumItem(mateAlbum = it!!) {
                            mainNavController.navigate(ScreenDestination.Album(it.id))
                        }
                    }*/
                }
            }
            item {
                ItemSubTitle(stringResource(id = R.string.popular_song))
            }
            songs?.let { list ->
                adaptiveItems(list.size) {
                    SongItem(dialogNavController = dialogNavController, song = list[it]) {
                        PlayManager.play(list, it)
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}