package com.zxhhyj.music.ui.screen.medialib

import android.os.Parcelable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.bean.WebDavSource
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.logic.utils.MediaLibHelper
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.screen.search.SearchScreenTabs
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTab
import com.zxhhyj.ui.view.AppTabRow
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
import kotlinx.parcelize.Parcelize

@Parcelize
private sealed class SourceTabs : Parcelable {
    data object Local : SourceTabs()
    data class WebDav(val webDavSource: WebDavSource) : SourceTabs()
}

private val SourceTabs.tabName: String
    @Composable get() = when (this) {
        SourceTabs.Local -> stringResource(id = R.string.all)
        is SourceTabs.WebDav -> this.webDavSource.remark.takeIf { it.isNotEmpty() }
            ?: stringResource(
                id = R.string.nothing
            )
    }

@Composable
fun MediaLibScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<SheetDestination>,
    paddingValues: PaddingValues
) {
    val tabs =
        (listOf(if (SettingRepository.EnableAndroidMediaLibs) SourceTabs.Local else null) + if (SettingRepository.EnableWebDav) WebDavMediaLibRepository.WebDavSources.map {
            SourceTabs.WebDav(it)
        } else emptyList()).filterNotNull()
    var showTab: SourceTabs? by remember {
        mutableStateOf(tabs.getOrNull(0))
    }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    AppScaffold(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding(),
        topBar = {
            val topBarState = LocalTopBarState.current
            AppTopBar(title = {
                Text(text = stringResource(id = R.string.media_lib),
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            lazyListState.scrollToItem(0)
                            topBarState.barOffsetHeightPx = 0f
                        }
                    })
            }, actions = {
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
                    sheetNavController.navigate(SheetDestination.MediaLibMenu)
                }) {
                    Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                }
            })
        }) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = paddingValues
        ) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemArrowRight(icon = {
                        Icon(
                            imageVector = Icons.Rounded.PlaylistPlay, contentDescription = null
                        )
                    },
                        text = { Text(text = stringResource(id = R.string.play_list)) },
                        subText = { }) {
                        mainNavController.navigate(ScreenDestination.PlayList)
                    }
                    ItemDivider()
                    ItemArrowRight(icon = {
                        Icon(
                            imageVector = Icons.Rounded.Album, contentDescription = null
                        )
                    },
                        text = { Text(text = stringResource(id = R.string.album)) },
                        subText = { }) {
                        mainNavController.navigate(ScreenDestination.Album)
                    }
                    ItemDivider()
                    ItemArrowRight(icon = {
                        Icon(
                            imageVector = Icons.Rounded.Mic, contentDescription = null
                        )
                    },
                        text = { Text(text = stringResource(id = R.string.singer)) },
                        subText = { }) {
                        mainNavController.navigate(ScreenDestination.Singer)
                    }
                    ItemDivider()
                    ItemArrowRight(icon = {
                        Icon(
                            imageVector = Icons.Rounded.Folder, contentDescription = null
                        )
                    },
                        text = { Text(text = stringResource(id = R.string.folder)) },
                        subText = { }) {
                        mainNavController.navigate(ScreenDestination.Folder)
                    }
                }
            }
            item {
                tabs.takeIf { it.size > 1 }?.let {
                    AppTabRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        it.forEach { tab ->
                            AppTab(selected = tab == showTab, onClick = { showTab = tab }) {
                                Text(text = tab.tabName)
                            }
                        }
                    }
                } ?: ItemSpacer()
            }

            roundColumn {
                when (val tab = showTab) {
                    SourceTabs.Local -> {
                        val list = MediaLibHelper.songs
                        itemsIndexed(list, contentType = { _: Int, item: SongBean ->
                            item
                        }) { index, item ->
                            SongItem(sheetNavController = sheetNavController, songBean = item) {
                                PlayerManager.play(list, index)
                            }
                        }
                    }

                    is SourceTabs.WebDav -> {
                        val list = MediaLibHelper.songs.filterIsInstance<SongBean.WebDav>().filter {
                            it.webDavSource == tab.webDavSource
                        }
                        itemsIndexed(list, contentType = { _: Int, item: SongBean ->
                            item
                        }) { index, item ->
                            SongItem(sheetNavController = sheetNavController, songBean = item) {
                                PlayerManager.play(list, index)
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}
