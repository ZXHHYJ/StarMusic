package com.zxhhyj.music.ui.sheet

import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.MainApplication
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.view.AppListButton
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popAll
import kotlinx.coroutines.launch

@Composable
fun SongMenuSheet(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    song: SongBean,
) {
    val deleteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            if (it.resultCode == -1) {
                AndroidMediaLibsRepository.delete(song)
                sheetNavController.popAll()
            }
        }
    )
    val coroutineScope = rememberCoroutineScope()
    val currentSong by PlayManager.currentSongLiveData().observeAsState()
    LazyColumn {
        item {
            AppListButton(
                onClick = {
                    sheetNavController.popAll()
                    mainNavController.navigate(ScreenDestination.AlbumCnt(song.album))
                },
                imageVector = Icons.Rounded.Album,
                text = "${stringResource(id = R.string.album)}:${song.album.name}"
            )
        }
        item {
            AppListButton(
                onClick = {
                    sheetNavController.popAll()
                    mainNavController.navigate(ScreenDestination.SingerCnt(song.artist))
                },
                imageVector = Icons.Rounded.Person,
                text = "${stringResource(id = R.string.singer)}:${song.artist.name}"
            )
        }
        item {
            AppListButton(
                onClick = {
                    sheetNavController.popAll()
                    sheetNavController.navigate(BottomSheetDestination.AddToPlayList(song))
                },
                imageVector = Icons.Rounded.Add,
                text = stringResource(id = R.string.add_to_playlist)
            )
        }
        item {
            AppListButton(
                onClick = {
                    sheetNavController.popAll()
                    PlayManager.addNextPlay(song)
                },
                imageVector = Icons.Rounded.AddToQueue,
                text = stringResource(id = R.string.next_play)
            )
        }
        item {
            AppListButton(
                onClick = {
                    sheetNavController.popAll()
                    sheetNavController.navigate(BottomSheetDestination.SongInfo(song))
                },
                imageVector = Icons.Rounded.Info,
                text = stringResource(id = R.string.song_info)
            )
        }
        if (currentSong != song) {
            item {
                AppListButton(
                    onClick = {
                        sheetNavController.popAll()
                        AndroidMediaLibsRepository.hide(song)
                    },
                    imageVector = Icons.Rounded.HideSource,
                    text = stringResource(id = R.string.hide)
                )
            }
            item {
                AppListButton(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            coroutineScope.launch {
                                val deleteUri =
                                    ContentUris.withAppendedId(
                                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                        song.id
                                    )
                                val pendingIntent = MediaStore.createDeleteRequest(
                                    MainApplication.context.contentResolver,
                                    listOf(deleteUri)
                                )
                                val request =
                                    IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                                deleteLauncher.launch(request)
                            }
                        }
                    },
                    imageVector = Icons.Rounded.Delete,
                    text = stringResource(id = R.string.delete)
                )
            }
        }
    }
}
