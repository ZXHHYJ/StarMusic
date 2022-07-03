package studio.mandysa.music.ui.screen.browse

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppLazyVerticalGrid
import studio.mandysa.music.ui.common.SearchBar
import studio.mandysa.music.ui.common.StateLayout
import studio.mandysa.music.ui.item.ItemSubTitle
import studio.mandysa.music.ui.item.ItemTitle
import studio.mandysa.music.ui.item.PlaylistItem
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BannerItem(typeTitle: String, bannerUrl: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = cornerShape,
            modifier = Modifier
                .height(140.dp)
        ) {
            AsyncImage(
                model = bannerUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = typeTitle,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = textColor,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BrowseScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    browseViewModel: BrowseViewModel = viewModel()
) {
    val bannerItems by browseViewModel.bannersLiveData.observeAsState(arrayListOf())
    val recommendSongs by browseViewModel.recommendSongLiveData.observeAsState(arrayListOf())
    val recommendPlaylist by browseViewModel.recommendPlaylistLiveData.observeAsState(arrayListOf())
    val playlistSquare by browseViewModel.playlistSquareLiveData.observeAsState(arrayListOf())
    StateLayout(viewModel = browseViewModel) {
        AppLazyVerticalGrid(modifier = Modifier.fillMaxSize()) {
            item {
                ItemTitle(stringResource(R.string.browse))
            }
            item {
                SearchBar(onClick = { mainNavController.navigate(ScreenDestination.Search) }) {
                    Text(text = stringResource(id = R.string.search_hint))
                }
            }
            item {
                if (isMedium) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = horizontalMargin),
                        horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2)
                    ) {
                        items(bannerItems) {
                            BannerItem(typeTitle = it.typeTitle, bannerUrl = it.pic)
                        }
                    }
                } else {
                    Column {
                        val pagerState = rememberPagerState()
                        HorizontalPager(
                            count = bannerItems.size,
                            itemSpacing = horizontalMargin,
                            contentPadding = PaddingValues(horizontal = horizontalMargin),
                            state = pagerState
                        ) {
                            val model = bannerItems[it]
                            BannerItem(typeTitle = model.typeTitle, bannerUrl = model.pic)
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            activeColor = onBackground,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(horizontal = horizontalMargin),
                        )
                    }
                }
            }
            item {
                ItemSubTitle(stringResource(R.string.recommend_playlist))
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalMargin),
                    horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2),
                ) {
                    items(recommendPlaylist) { model ->
                        PlaylistItem(title = model.name, coverUrl = model.picUrl) {
                            mainNavController.navigate(ScreenDestination.Playlist(model.id))
                        }
                    }
                }
            }
            item {
                ItemSubTitle(stringResource(R.string.playlist_square))
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalMargin),
                    horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2),
                ) {
                    items(playlistSquare) { model ->
                        PlaylistItem(title = model.name, coverUrl = model.picUrl) {
                            mainNavController.navigate(ScreenDestination.Playlist(model.id))
                        }
                    }
                }
            }
            item {
                ItemSubTitle(stringResource(R.string.recommend_song))
            }
            autoItems(recommendSongs.size) {
                SongItem(dialogNavController, recommendSongs[it]) {
                    PlayManager.loadPlaylist(recommendSongs, it)
                }
            }
            item {
                Spacer(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}