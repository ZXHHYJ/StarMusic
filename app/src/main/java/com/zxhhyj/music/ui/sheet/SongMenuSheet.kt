package com.zxhhyj.music.ui.sheet


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddToQueue
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.HideSource
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository.rememberMediaLibsManager
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.ui.common.AppMenuButton
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.sheet.item.HeadSongTitleItem
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
            HeadSongTitleItem(song = song)
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
                    sheetNavController.navigate(BottomSheetDestination.AddToPlayList(song))
                },
                imageVector = Icons.Rounded.Add,
                text = stringResource(id = R.string.add_to_playlist)
            )
        }
        item {
            AppMenuButton(
                onClick = {
                    sheetNavController.popAll()
                    PlayManager.addNextPlay(song)
                },
                imageVector = Icons.Rounded.AddToQueue,
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
