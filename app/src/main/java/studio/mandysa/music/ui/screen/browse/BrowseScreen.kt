package studio.mandysa.music.ui.screen.browse

import android.os.Parcelable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.rememberNavController
import kotlinx.parcelize.Parcelize
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.item.ItemSubTitle
import studio.mandysa.music.ui.item.ItemTitle
import studio.mandysa.music.ui.item.PlaylistItem
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.playlist.PlaylistScreen
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.round

sealed class BrowseScreenDestination : Parcelable {
    @Parcelize
    object Main : BrowseScreenDestination()

    @Parcelize
    data class Playlist(val id: String) : BrowseScreenDestination()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BannerItem(typeTitle: String, bannerUrl: String) {
    Column {
        Card(
            shape = RoundedCornerShape(round),
            modifier = Modifier
                .height(140.dp)
                .padding(horizontal = horizontalMargin)
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
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BrowseScreen(browseViewModel: BrowseViewModel = viewModel()) {
    val navController =
        rememberNavController<BrowseScreenDestination>(startDestination = BrowseScreenDestination.Main)
    NavBackHandler(navController)
    NavHost(navController) { it ->
        when (it) {
            BrowseScreenDestination.Main -> {
                val bannerItems by browseViewModel.banners.observeAsState(listOf())
                val recommendSongs by browseViewModel.recommendSongs.observeAsState(listOf())
                val recommendPlaylist by browseViewModel.recommendPlaylist.observeAsState(listOf())
                val playlistSquare by browseViewModel.playlistSquare.observeAsState(listOf())
                LazyColumn {
                    item {
                        ItemTitle(stringResource(R.string.browse))
                    }
                    item {
                        HorizontalPager(count = bannerItems.size) {
                            val model = bannerItems[it]
                            BannerItem(typeTitle = model.typeTitle, bannerUrl = model.pic)
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
                                    navController.navigate(BrowseScreenDestination.Playlist(model.id))
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
                                    navController.navigate(BrowseScreenDestination.Playlist(model.id))
                                }
                            }
                        }
                    }
                    item {
                        ItemSubTitle(stringResource(R.string.recommend_song))
                    }
                    items(recommendSongs.size) {
                        SongItem(recommendSongs[it]) {
                            PlayManager.loadPlaylist(recommendSongs, it)
                            PlayManager.play()
                        }
                    }
                }
            }
            is BrowseScreenDestination.Playlist -> {
                PlaylistScreen(navController, it.id)
            }
        }
    }
}