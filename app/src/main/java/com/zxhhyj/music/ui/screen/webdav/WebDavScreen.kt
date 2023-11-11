package com.zxhhyj.music.ui.screen.webdav

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.webdavmanager.WebDavManager
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.screen.webdav.WebDavScreenTabs.*
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppTab
import com.zxhhyj.ui.view.AppTabRow
import com.zxhhyj.ui.view.RoundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import kotlinx.coroutines.launch

enum class WebDavScreenTabs {
    All, CloudBased, Downloaded
}

val WebDavScreenTabs.tabName: String
    @Composable get() = when (this) {
        All -> stringResource(id = R.string.all)
        CloudBased -> stringResource(id = R.string.cloud_based)
        Downloaded -> stringResource(id = R.string.downloaded)
    }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WebDavScreen(
    paddingValues: PaddingValues,
    sheetNavController: NavController<SheetDestination>,
    dialogNavController: NavController<DialogDestination>,
    mainNavController: NavController<ScreenDestination>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues)
    ) {
        AppCenterTopBar(
            modifier = Modifier,
            title = { Text(text = stringResource(id = R.string.webdav)) },
            actions = {
                AppIconButton(onClick = { dialogNavController.navigate(DialogDestination.SyncWebDavMediaLib) }) {
                    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
                }
            }
        )
        RoundColumn(modifier = Modifier.fillMaxWidth()) {
            val coroutineScope = rememberCoroutineScope()
            val pagerState = rememberPagerState(
                initialPage = 0,
            ) { values().size }
            AppTabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage
            ) {
                values()
                    .forEachIndexed { index, searchTypeTabDestination ->
                        AppTab(
                            selected = index == pagerState.currentPage,
                            onClick = {
                                coroutineScope.launch { pagerState.animateScrollToPage(index) }
                            }
                        ) {
                            Text(text = searchTypeTabDestination.tabName)
                        }
                    }
            }

            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
                userScrollEnabled = true
            ) { t ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    when (values()[t]) {
                        All -> {
                            items(WebDavMediaLibRepository.davFileList) { it ->
                                SongItem(
                                    sheetNavController = sheetNavController,
                                    webDavFile = it,
                                    onClick = {
                                        WebDavManager.download(it)
                                    },
                                    downloadedOnClick = {
                                        PlayManager.play(listOf(it), 0)
                                    })
                            }
                        }

                        CloudBased -> {
                            items(WebDavMediaLibRepository.davFileList.filter { webDavFile ->
                                WebDavMediaLibRepository.songs.find {
                                    it.webDavFile == webDavFile
                                } == null
                            }) { it ->
                                SongItem(
                                    sheetNavController = sheetNavController,
                                    webDavFile = it,
                                    onClick = {
                                        WebDavManager.download(it)
                                    },
                                    downloadedOnClick = {
                                        PlayManager.play(listOf(it), 0)
                                    })
                            }
                        }

                        Downloaded -> {
                            items(WebDavMediaLibRepository.davFileList.filter { webDavFile ->
                                WebDavMediaLibRepository.songs.find {
                                    it.webDavFile == webDavFile
                                } != null
                            }) { it ->
                                SongItem(
                                    sheetNavController = sheetNavController,
                                    webDavFile = it,
                                    onClick = {
                                        WebDavManager.download(it)
                                    },
                                    downloadedOnClick = {
                                        PlayManager.play(listOf(it), 0)
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}