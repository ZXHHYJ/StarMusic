package studio.mandysa.music.ui.screen.me

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.common.SwipeRefreshLayout
import studio.mandysa.music.ui.item.ItemSubTitle
import studio.mandysa.music.ui.item.ItemTitle
import studio.mandysa.music.ui.item.PlaylistItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.cornerShape
import studio.mandysa.music.ui.theme.horizontalMargin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    meViewModel: MeViewModel = viewModel()
) {
    val userInfo by meViewModel.userInfoLiveData.observeAsState()
    val playlist by meViewModel.allPlaylistLiveData.observeAsState()
    //val playHistory by PlayHistoryRepository.playHistoryLiveData.observeAsState()
    SwipeRefreshLayout(viewModel = meViewModel) {
        LazyColumn {
            item {
                ItemTitle(stringResource(R.string.me))
            }
            item {
                userInfo?.let {
                    Card(
                        modifier = Modifier.padding(horizontal = horizontalMargin),
                        shape = cornerShape
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clickable {
                                    dialogNavController.navigate(DialogDestination.MeMenu)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                                AppAsyncImage(size = 70.dp, 35.dp, url = it.avatarUrl)
                            }
                            Column {
                                Text(text = it.nickname)
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(text = it.signature)
                            }
                        }
                    }
                }
            }
            item {
                ItemSubTitle(stringResource(R.string.me_playlist))
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalMargin),
                    horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2)
                ) {
                    playlist?.let { it ->
                        items(it) {
                            PlaylistItem(title = it.name, coverUrl = it.coverImgUrl) {
                                mainNavController.navigate(ScreenDestination.Playlist(it.id))
                            }
                        }
                    }
                }
            }
            item {
                ItemSubTitle(stringResource(R.string.recently_played))
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalMargin),
                    horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2)
                ) {
                    // TODO: 播放历史 
                   /* playHistory?.let { it ->
                        items(it) {
                            PlaylistItem(title = it.title, coverUrl = it.coverUrl) {
                                mainNavController.navigate(ScreenDestination.Playlist(it.album.id))
                            }
                        }
                    }*/
                }
            }
            item {
                ItemSubTitle(stringResource(R.string.more))
            }
            item {
                MenuItem(
                    modifier = Modifier.padding(horizontal = horizontalMargin),
                    title = stringResource(id = R.string.setting),
                    imageVector = Icons.Rounded.Favorite
                ) {
                    mainNavController.navigate(ScreenDestination.Setting)
                }
            }
            item {
                MenuItem(
                    modifier = Modifier.padding(horizontal = horizontalMargin),
                    title = stringResource(id = R.string.about),
                    imageVector = Icons.Rounded.Info
                ) {
                    mainNavController.navigate(ScreenDestination.About)
                }
            }
            item {
                Spacer(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}