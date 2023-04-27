package com.zxhhyj.music.ui.sheet


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.HideSource
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.media.MediaLibsRepository.rememberMediaLibsManager
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.ui.common.button.AppMenuButton
import com.zxhhyj.music.ui.common.card.AppCard
import com.zxhhyj.music.ui.common.image.AppAsyncImage
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.textColor
import com.zxhhyj.music.ui.theme.textColorLight
import com.zxhhyj.music.ui.theme.vertical
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popAll

@Composable
fun SongMenuSheet(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    song: SongBean,
) {
    val currentSong by PlayManager.changeMusicLiveData().observeAsState()
    val mediaLibsManager = rememberMediaLibsManager()
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
                        .height(70.dp)
                ) {
                    AppCard(backgroundColor = Color.Transparent, modifier = Modifier.size(70.dp)) {
                        AppAsyncImage(modifier = Modifier.fillMaxSize(), url = song.album.coverUrl)
                    }
                    Column(modifier = Modifier.padding(vertical)) {
                        Text(
                            text = song.songName,
                            color = textColor,
                            fontSize = 15.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.weight(1.0f))
                        Text(
                            text = song.artist.name,
                            color = textColorLight,
                            fontSize = 13.sp,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
        item {
            AppMenuButton(
                onClick = {
                    sheetNavController.popAll()
                    mainNavController.navigate(ScreenDestination.AlbumCnt(song.album))
                },
                imageVector = Icons.Rounded.Album,
                text = "${stringResource(id = R.string.album)}:${song.album.name}"
            )
        }
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
        item {
            AppMenuButton(
                onClick = {
                    sheetNavController.popAll()
                    sheetNavController.navigate(BottomSheetDestination.SongInfo(song))
                },
                imageVector = Icons.Rounded.Info,
                text = stringResource(id = R.string.song_info)
            )
        }
        item {
            AppMenuButton(
                onClick = {
                    sheetNavController.popAll()
                    mediaLibsManager.hide(song)
                },
                imageVector = Icons.Rounded.HideSource,
                text = stringResource(id = R.string.hide),
                enabled = currentSong != song
            )
        }
        item {
            AppMenuButton(
                onClick = {
                    sheetNavController.popAll()
                    mediaLibsManager.delete(song)
                },
                imageVector = Icons.Rounded.Delete,
                text = stringResource(id = R.string.delete), enabled = currentSong != song
            )
        }
    }
}
