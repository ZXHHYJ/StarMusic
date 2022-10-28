package studio.mandysa.music.ui.screen.me

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Star
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
import studio.mandysa.music.ui.common.AppCard
import studio.mandysa.music.ui.common.AppRoundAsyncImage
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.common.Preview
import studio.mandysa.music.ui.item.ItemSubTitle
import studio.mandysa.music.ui.item.PlaylistItem
import studio.mandysa.music.ui.item.RoundIconItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.verticalMargin

@Composable
fun MeScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    meViewModel: MeViewModel = viewModel()
) {
    val userInfo by meViewModel.userInfoLiveData.observeAsState()
    val userPlaylists by meViewModel.userPlaylistBeanLiveData.observeAsState()
    //val playHistory by PlayHistoryRepository.playHistoryLiveData.observeAsState()
    Preview(
        modifier = Modifier
            .padding(paddingValues)
            .statusBarsPadding(),
        refresh = {
            meViewModel.refresh()
        }
    ) {
        LazyColumn {
            item {
                userInfo?.let {
                    AppCard(
                        modifier = Modifier
                            .padding(horizontal = horizontalMargin)
                            .padding(top = verticalMargin),
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
                                AppRoundAsyncImage(size = 70.dp, url = it.avatarUrl)
                            }
                            Column {
                                Text(text = it.nickname, color = textColor)
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(text = it.signature, color = textColor)
                            }
                        }
                    }
                }
            }
            item {
                LazyRow(
                    modifier = Modifier.padding(top = 10.dp),
                    contentPadding = PaddingValues(horizontal = horizontalMargin),
                    horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2)
                ) {
                    item {
                        RoundIconItem(icon = Icons.Rounded.Favorite, title = "我喜欢") {

                        }
                    }
                    item {
                        RoundIconItem(icon = Icons.Rounded.Download, title = "本地") {

                        }
                    }
                    item {
                        RoundIconItem(icon = Icons.Rounded.Album, title = "歌单") {
                            mainNavController.navigate(ScreenDestination.MePlaylist)
                        }
                    }
                    item {
                        RoundIconItem(icon = Icons.Rounded.Check, title = "已购") {

                        }
                    }
                    item {
                        RoundIconItem(icon = Icons.Rounded.Star, title = "收藏的专辑") {

                        }
                    }
                }
            }
            item {
                ItemSubTitle(
                    stringResource(R.string.me_playlist),
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalMargin),
                    horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2)
                ) {
                    item {
                        PlaylistItem(
                            icon = Icons.Rounded.Add
                        ) {
                            mainNavController.navigate(ScreenDestination.MePlaylist)
                        }
                    }
                    userPlaylists?.let { it ->
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
                    item {
                        PlaylistItem(
                            icon = Icons.Rounded.MoreVert
                        ) {
                            //mainNavController.navigate(ScreenDestination.MePlaylist)
                        }
                    }
                    /* playHistory?.let { it ->
                         items(it) {
                             PlaylistItem(title = it.title, coverUrl = it.coverUrl) {
                                 mainNavController.navigate(ScreenDestination.Playlist(it.album.id))
                             }
                         }
                     }*/
                }
            }
            /*item {
                ItemSubTitle(stringResource(R.string.my_digital_album))
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalMargin),
                    horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2)
                ) {
                    myDigitalAlbums?.let { it ->
                        items(it) {
                            PlaylistItem(title = it.albumName, coverUrl = it.cover) {
                                mainNavController.navigate(ScreenDestination.Album(it.albumId))
                            }
                        }
                    }
                }
            }*/
            item {
                ItemSubTitle(stringResource(R.string.more))
            }
            item {
                MenuItem(
                    modifier = Modifier.padding(horizontal = horizontalMargin),
                    title = stringResource(id = R.string.setting),
                    imageVector = Icons.Rounded.Settings
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