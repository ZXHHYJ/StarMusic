package com.zxhhyj.music.ui.screen.medialib

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.item.SubTitleItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.screen.search.SearchScreenTabs
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTopBar
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.toolbarHeight
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import java.util.Collections

@Composable
fun MediaLibScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<SheetDestination>,
    paddingValues: PaddingValues
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.media_lib),
                content = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.media_lib),
                            color = LocalColorScheme.current.text,
                            fontSize = 26.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = horizontal)
                                .height(toolbarHeight)
                        )
                        RoundColumn(modifier = Modifier.fillMaxWidth()) {
                            ItemArrowRight(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.PlaylistPlay,
                                        contentDescription = null
                                    )
                                },
                                text = { Text(text = stringResource(id = R.string.play_list)) },
                                subText = { }) {
                                mainNavController.navigate(ScreenDestination.PlayList)
                            }
                            ItemDivider()
                            ItemArrowRight(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Album,
                                        contentDescription = null
                                    )
                                },
                                text = { Text(text = stringResource(id = R.string.album)) },
                                subText = { }) {
                                mainNavController.navigate(ScreenDestination.Album)
                            }
                            ItemDivider()
                            ItemArrowRight(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Mic,
                                        contentDescription = null
                                    )
                                },
                                text = { Text(text = stringResource(id = R.string.singer)) },
                                subText = { }) {
                                mainNavController.navigate(ScreenDestination.Singer)
                            }
                            ItemDivider()
                            ItemArrowRight(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Folder,
                                        contentDescription = null
                                    )
                                },
                                text = { Text(text = stringResource(id = R.string.folder)) },
                                subText = { }) {
                                mainNavController.navigate(ScreenDestination.Folder)
                            }
                            if (SettingRepository.EnableWebDav) {
                                ItemDivider()
                                ItemArrowRight(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Rounded.CloudUpload,
                                            contentDescription = null
                                        )
                                    },
                                    text = { Text(text = stringResource(id = R.string.webdav)) },
                                    subText = { }) {
                                    mainNavController.navigate(ScreenDestination.WebDav)
                                }
                            }
                        }
                    }
                },
                actions = {
                    AppIconButton(onClick = {
                        mainNavController.navigate(
                            ScreenDestination.Search(
                                SearchScreenTabs.Single
                            )
                        )
                    }) {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                    }
                    AppIconButton(onClick = {
                        sheetNavController.navigate(SheetDestination.SongSort)
                    }) {
                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                    }
                }
            )
        }) {
        Column(modifier = Modifier.fillMaxSize()) {
            SubTitleItem(title = stringResource(id = R.string.single))
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    val list =
                        (AndroidMediaLibRepository.songs + WebDavMediaLibRepository.songs).sortedWith(
                            compareBy<SongBean> {
                                when (SettingRepository.SongSort) {
                                    SettingRepository.SongSortType.SONG_NAME.value -> it.songName
                                    SettingRepository.SongSortType.SONG_DURATION.value -> it.duration
                                    SettingRepository.SongSortType.SINGER_NAME.value -> it.artist.name
                                    SettingRepository.SongSortType.DATE_MODIFIED.value -> it.dateModified
                                    else -> null
                                }
                            }.let { comparator ->
                                if (SettingRepository.Descending) {
                                    Collections.reverseOrder(comparator)
                                } else {
                                    comparator
                                }
                            })
                    itemsIndexed(list) { index, item ->
                        SongItem(sheetNavController = sheetNavController, songBean = item) {
                            PlayManager.play(list, index)
                        }
                    }
                }
            }
        }

    }
}
