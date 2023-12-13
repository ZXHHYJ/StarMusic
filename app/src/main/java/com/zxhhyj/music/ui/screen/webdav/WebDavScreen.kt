package com.zxhhyj.music.ui.screen.webdav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.service.webdavmanager.WebDavManager
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.screen.webdav.WebDavScreenTabs.All
import com.zxhhyj.music.ui.screen.webdav.WebDavScreenTabs.CloudBased
import com.zxhhyj.music.ui.screen.webdav.WebDavScreenTabs.Downloaded
import com.zxhhyj.music.ui.screen.webdav.WebDavScreenTabs.entries
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppTab
import com.zxhhyj.ui.view.AppTabRow
import com.zxhhyj.ui.view.RoundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.moveToTop
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.rememberNavController

enum class WebDavScreenTabs {
    All, CloudBased, Downloaded
}

val WebDavScreenTabs.tabName: String
    @Composable get() = when (this) {
        All -> stringResource(id = R.string.all)
        CloudBased -> stringResource(id = R.string.cloud_based)
        Downloaded -> stringResource(id = R.string.downloaded)
    }

@Composable
fun WebDavScreen(
    paddingValues: PaddingValues,
    sheetNavController: NavController<SheetDestination>,
    dialogNavController: NavController<DialogDestination>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues)
    ) {
        AppCenterTopBar(
            title = { Text(text = stringResource(id = R.string.webdav)) },
            actions = {
                AppIconButton(onClick = { dialogNavController.navigate(DialogDestination.SyncWebDavMediaLib) }) {
                    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
                }
            }
        )

        val navController =
            rememberNavController(startDestination = All)
        AppTabRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            entries
                .forEach { destination ->
                    AppTab(
                        selected = destination == navController.backstack.entries.last().destination,
                        onClick = {
                            if (!navController.moveToTop {
                                    it == destination
                                }) {
                                navController.navigate(destination)
                            }
                        }
                    ) {
                        Text(text = destination.tabName)
                    }
                }
        }
        RoundColumn(modifier = Modifier.fillMaxWidth()) {
            NavHost(modifier = Modifier.fillMaxWidth(), controller = navController) { it ->
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    when (it) {
                        All -> {
                            items(WebDavMediaLibRepository.davFileList) { it ->
                                SongItem(
                                    sheetNavController = sheetNavController,
                                    webDavFile = it,
                                    onClick = {
                                        WebDavManager.download(it)
                                    },
                                    downloadedOnClick = {
                                        PlayerManager.play(listOf(it), 0)
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
                                        PlayerManager.play(listOf(it), 0)
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
                                        PlayerManager.play(listOf(it), 0)
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}