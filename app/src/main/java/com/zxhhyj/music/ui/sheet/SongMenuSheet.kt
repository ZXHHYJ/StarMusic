package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.service.media.playmanager.PlayManager
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popAll
import java.io.File

@Composable
fun SongMenuSheet(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<SheetDestination>,
    song: SongBean,
) {
    val currentSong by PlayManager.currentSongLiveData().observeAsState()
    LazyColumn {
        item {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                ItemArrowRight(
                    icon = { Icon(imageVector = Icons.Rounded.Album, contentDescription = null) },
                    text = { Text(text = song.album.name) },
                    subText = { Text(text = stringResource(id = R.string.album)) }) {
                    sheetNavController.popAll()
                    mainNavController.navigate(ScreenDestination.AlbumCnt(song.album))
                }
                ItemDivider()
                ItemArrowRight(
                    icon = { Icon(imageVector = Icons.Rounded.Person, contentDescription = null) },
                    text = { Text(text = song.artist.name) },
                    subText = { Text(text = stringResource(id = R.string.singer)) }) {
                    sheetNavController.popAll()
                    mainNavController.navigate(ScreenDestination.SingerCnt(song.artist))
                }
            }
        }
        item {
            ItemSpacer()
        }
        item {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                Item(
                    icon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = null) },
                    text = { Text(text = stringResource(id = R.string.add_to_playlist)) },
                    subText = { }) {
                    sheetNavController.popAll()
                    sheetNavController.navigate(SheetDestination.AddToPlayList(song))
                }
                ItemDivider()
                Item(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.AddToQueue,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.next_play)) },
                    subText = { }) {
                    sheetNavController.popAll()
                    PlayManager.addNextPlay(song)
                }
                ItemDivider()
                Item(
                    icon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = null) },
                    text = {
                        Text(
                            text = stringResource(id = R.string.song_info)
                        )
                    },
                    subText = { }) {
                    sheetNavController.popAll()
                    sheetNavController.navigate(SheetDestination.SongInfo(song))
                }
                ItemDivider()
                if (song is SongBean.Local) {
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.HideSource,
                                contentDescription = null
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(id = R.string.hide)
                            )
                        },
                        subText = { },
                        enabled = currentSong != song
                    ) {
                        sheetNavController.popAll()
                        AndroidMediaLibRepository.hideSong(song)
                    }
                    ItemDivider()
                }
                Item(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(id = R.string.delete)
                        )
                    },
                    subText = { },
                    enabled = currentSong != song
                ) {
                    when (song) {
                        is SongBean.Local -> {
                            val file = File(song.data)
                            file.delete()
                            AndroidMediaLibRepository.removeSong(song)
                        }

                        is SongBean.WebDav -> {
                            val file = File(song.data)
                            file.delete()
                            WebDavMediaLibRepository.removeSong(song)
                        }

                        else -> {}
                    }
                    sheetNavController.popAll()
                }
            }
        }
    }
}
