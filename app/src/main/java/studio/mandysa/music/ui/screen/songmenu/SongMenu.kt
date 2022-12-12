package studio.mandysa.music.ui.screen.songmenu


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popAll
import kotlinx.parcelize.RawValue
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.service.playmanager.ktx.*
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.AppDialog
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.textColorLight
import studio.mandysa.music.ui.theme.verticalMargin


@Composable
fun SongMenu(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    song: @RawValue SongBean,
    songMenuViewModel: SongMenuViewModel = viewModel()
) {
    val isLike by songMenuViewModel.likedLiveData.observeAsState()
    AppDialog {
        LazyColumn {
            item {
                Box(
                    modifier = Modifier.padding(
                        horizontal = horizontalMargin,
                        vertical = verticalMargin
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .height(80.dp)
                    ) {
                        AppAsyncImage(modifier = Modifier.size(80.dp), url = song.coverUrl)
                        Column(modifier = Modifier.padding(verticalMargin)) {
                            Text(
                                text = song.title,
                                color = textColor,
                                fontSize = 15.sp,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.weight(1.0f))
                            Text(
                                text = song.artist.allArtist(),
                                color = textColorLight,
                                fontSize = 13.sp,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
            item {
                MenuItem(
                    modifier = Modifier.padding(horizontal = horizontalMargin),
                    title = stringResource(id = if (isLike == true) R.string.remove_like else R.string.add_like),
                    imageVector = if (isLike == true) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    enabled = isLike != null
                ) {
                    songMenuViewModel.likeMusic(isLike == false)
                }
            }
            item {
                MenuItem(
                    modifier = Modifier.padding(horizontal = horizontalMargin),
                    title = "${stringResource(id = R.string.album)}:${song.album.name}",
                    imageVector = Icons.Rounded.Album
                ) {
                    dialogNavController.popAll()
                    // TODO:
                    //mainNavController.navigate(ScreenDestination.AlbumCnt(song.album.id))
                }
            }
            item {
                MenuItem(
                    modifier = Modifier.padding(horizontal = horizontalMargin),
                    title = stringResource(id = R.string.next_play),
                    imageVector = Icons.Rounded.Add
                ) {
                    dialogNavController.popAll()
                    PlayManager.addNextPlay(song)
                }
            }
            when (song) {
                is SongBean.Local -> {
                    item {
                        MenuItem(
                            modifier = Modifier.padding(horizontal = horizontalMargin),
                            title = "${stringResource(id = R.string.singer)}:${song.artist}",
                            imageVector = Icons.Rounded.Person
                        ) {
                            dialogNavController.popAll()
                            //mainNavController.navigate(ScreenDestination.SingerCnt(song.artist))
                        }
                    }
                }
                is SongBean.Network -> {
                    items(song.artist) {
                        MenuItem(
                            modifier = Modifier.padding(horizontal = horizontalMargin),
                            title = "${stringResource(id = R.string.singer)}:${it.name}",
                            imageVector = Icons.Rounded.Person
                        ) {
                            dialogNavController.popAll()
                            mainNavController.navigate(ScreenDestination.CloudSingerCnt(it))
                        }
                    }
                }
            }
        }
    }
}
