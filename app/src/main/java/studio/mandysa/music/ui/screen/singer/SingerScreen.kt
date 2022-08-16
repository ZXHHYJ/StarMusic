package studio.mandysa.music.ui.screen.singer

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppLazyVerticalGrid
import studio.mandysa.music.ui.common.AppMediumTopAppBar
import studio.mandysa.music.ui.common.AppScaffold
import studio.mandysa.music.ui.common.Preview
import studio.mandysa.music.ui.item.AlbumItem
import studio.mandysa.music.ui.item.ContentColumnItem
import studio.mandysa.music.ui.item.ItemSubTitle
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin

@OptIn(ExperimentalMaterial3Api::class)
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
    val albums = viewModel.albumSource.collectAsLazyPagingItems()
    val songs = viewModel.singerHotSongs
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarState()
    )
    AppScaffold(modifier = Modifier
        .statusBarsPadding()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppMediumTopAppBar(
                title = {
                    Text(text = singerDetails?.name ?: stringResource(id = R.string.singer))
                },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.pop() }) {
                        Icon(Icons.Rounded.ArrowBack, null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }) { it ->
        Preview(modifier = Modifier.padding(it), refresh = { viewModel.refresh() }) {
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
                songs?.let { list ->
                    adaptiveItems(list.size) {
                        SongItem(dialogNavController = dialogNavController, song = list[it]) {
                            PlayManager.play(list, it)
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}