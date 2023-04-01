package studio.mandysa.music.ui.sheet


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popAll
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.service.playmanager.ktx.*
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.AppCard
import studio.mandysa.music.ui.common.AppMenuButton
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontal
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.textColorLight
import studio.mandysa.music.ui.theme.vertical

/**
 * @author 黄浩
 */

@Composable
fun SongMenuSheet(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    song: SongBean,
) {
    LazyColumn {
        item {
            Box(
                modifier = Modifier.padding(
                    horizontal = horizontal,
                    vertical = vertical
                )
            ) {
                Row(
                    modifier = Modifier
                        .height(80.dp)
                ) {
                    AppCard(backgroundColor = Color.Transparent, modifier = Modifier.size(80.dp)) {
                        AppAsyncImage(modifier = Modifier.fillMaxSize(), url = song.coverUrl)
                    }
                    Column(modifier = Modifier.padding(vertical)) {
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
            /*val isLike = false
            MenuItem(
                modifier = Modifier.padding(horizontal = horizontal),
                title = stringResource(id = if (isLike == true) R.string.remove_like else R.string.add_like),
                imageVector = if (isLike == true) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                enabled = isLike != null
            ) {
                //songMenuViewModel.likeMusic(isLike == false)
            }*/
        }
        item {
            AppMenuButton(
                onClick = {
                    sheetNavController.popAll()
                    if (song is SongBean.Local) {
                        mainNavController.navigate(ScreenDestination.AlbumCnt(song.album))
                    }
                },
                imageVector = Icons.Rounded.Album,
                text = "${stringResource(id = R.string.album)}:${song.album.name}"
            )
        }
        item {
            AppMenuButton(
                onClick = {
                    sheetNavController.popAll()
                    PlayManager.addNextPlay(song)
                },
                imageVector = Icons.Rounded.Add,
                text = stringResource(id = R.string.next_play)
            )
        }
        if (song is SongBean.Local) {
            item {
                AppMenuButton(
                    onClick = {
                        sheetNavController.popAll()
                        mainNavController.navigate(ScreenDestination.SingerCnt(song.artist))
                    },
                    imageVector = Icons.Rounded.Person,
                    text = "${stringResource(id = R.string.singer)}:${song.artist.name}"
                )
            }
        }
    }
}
