package studio.mandysa.music.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.ui.item.ItemSubTitleScreen
import studio.mandysa.music.ui.item.ItemTitle
import studio.mandysa.music.ui.item.PlaylistItem
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.viewmodel.BrowseViewModel
import studio.mandysa.music.ui.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BannerItem(typeTitle: String, bannerUrl: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
        )
        {
            AsyncImage(
                model = bannerUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Text(
                    text = typeTitle,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("SetTextI18n")
@Composable
fun BrowseScreen(
    event: EventViewModel = viewModel(),
    viewModel: BrowseViewModel = viewModel()
) {
    val bannerItems by viewModel.getBanners().observeAsState(arrayListOf())
    val recommendSongs by viewModel.getRecommendSongs().observeAsState(arrayListOf())
    val recommendPlaylist by viewModel.getRecommendPlaylist().observeAsState(arrayListOf())
    val playlistSquare by viewModel.getPlaylistSquare().observeAsState(arrayListOf())
    val cookie = event.getCookieLiveData().observeAsState()
    if (cookie.value != null) {
        viewModel.initialize(cookie.value!!)
        viewModel.refresh()
    }
    LazyColumn {
        item {
            ItemTitle(stringResource(R.string.browse))
        }
        item {
            HorizontalPager(count = bannerItems.size) {
                val model = bannerItems[it]
                BannerItem(typeTitle = model.typeTitle, bannerUrl = model.pic) {
                    when (model.targetType) {
                        3000 -> {

                        }
                        1000 -> {

                        }
                        1 -> {

                        }
                        else -> {

                        }
                    }
                }
            }
        }
        item {
            ItemSubTitleScreen(stringResource(R.string.recommend_playlist))
        }
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = horizontalMargin),
                horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2),
            ) {
                items(recommendPlaylist) { model ->
                    PlaylistItem(title = model.name, coverUrl = model.picUrl) {

                    }
                }
            }
        }
        item {
            ItemSubTitleScreen(stringResource(R.string.playlist_square))
        }
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = horizontalMargin),
                horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2),
            ) {
                items(playlistSquare) { model ->
                    PlaylistItem(title = model.name, coverUrl = model.picUrl) {

                    }
                }
            }
        }
        item {
            ItemSubTitleScreen(stringResource(R.string.recommend_song))
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