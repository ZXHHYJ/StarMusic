package com.zxhhyj.music.ui.screen.medialib

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.MediaLibHelper
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.screen.search.SearchScreenTabs
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTopBar
import com.zxhhyj.ui.view.LocalTopBarState
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.roundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import kotlinx.coroutines.launch

@Composable
fun MediaLibScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<SheetDestination>,
    paddingValues: PaddingValues
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            val topBarState = LocalTopBarState.current
            AppTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(id = R.string.media_lib),
                        modifier = Modifier.clickable {
                            coroutineScope.launch {
                                lazyListState.scrollToItem(0)
                                topBarState.barOffsetHeightPx = 0f
                            }
                        })
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = lazyListState
        ) {
            item {
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
            item {
                ItemSpacer()
            }
            roundColumn {
                val list = MediaLibHelper.songs
                itemsIndexed(list, contentType = { _: Int, item: SongBean ->
                    item
                }) { index, item ->
                    SongItem(sheetNavController = sheetNavController, songBean = item) {
                        PlayerManager.play(list, index)
                    }
                }
            }
        }
    }
}
