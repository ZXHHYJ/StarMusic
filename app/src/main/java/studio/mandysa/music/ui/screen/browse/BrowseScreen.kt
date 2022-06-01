package studio.mandysa.music.ui.screen.browse

import android.annotation.SuppressLint
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
import dev.olshevski.navigation.reimagined.*
import kotlinx.parcelize.Parcelize
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.ktx.allArtist
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
@SuppressLint("SetTextI18n")
@Composable
private fun Main(
    navController: NavController<BrowseScreenDestination>,
    viewModel: BrowseViewModel = viewModel()
) {
    val bannerItems by viewModel.banners.observeAsState(listOf())
    val recommendSongs by viewModel.recommendSongs.observeAsState(listOf())
    val recommendPlaylist by viewModel.recommendPlaylist.observeAsState(listOf())
    val playlistSquare by viewModel.playlistSquare.observeAsState(listOf())
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
                modifier = Modifier.fillMaxWidth(),
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
                modifier = Modifier.fillMaxWidth(),
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
            val model = recommendSongs[it]
            SongItem(
                position = it + 1,
                title = model.title,
                singer = model.artist.allArtist()
            ) {
                PlayManager.loadPlaylist(recommendSongs, it)
                PlayManager.play()
            }
        }
    }
}


@Composable
fun BrowseScreen() {
    val navController =
        rememberNavController<BrowseScreenDestination>(startDestination = BrowseScreenDestination.Main)
    NavBackHandler(navController)
    NavHost(navController) { screen ->
        when (screen) {
            BrowseScreenDestination.Main -> {
                Main(navController)
            }
            is BrowseScreenDestination.Playlist -> {
                PlaylistScreen(navController, id = screen.id)
            }
        }
    }
}