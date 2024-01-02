package com.zxhhyj.music.ui.screen.webdavfoldermanager

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.InsertDriveFile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.thegrizzlylabs.sardineandroid.DavResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository.addFolder
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository.removeFolder
import com.zxhhyj.music.logic.utils.SardineUtils.toSardine
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppSwitch
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemSubTitle
import com.zxhhyj.ui.view.roundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@Composable
fun WebDavFolderManagerScreen(
    paddingValues: PaddingValues,
    mainNavController: NavController<ScreenDestination>,
    indexInWebDavSourceList: Int,
    path: String
) {
    val webDavSource by rememberSaveable(WebDavMediaLibRepository.webDavSources[indexInWebDavSourceList]) {
        mutableStateOf(WebDavMediaLibRepository.webDavSources[indexInWebDavSourceList])
    }
    val sardine = webDavSource.toSardine()
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.folder_manager)) })
        }) {
        val folderProduceState by produceState<List<DavResource>?>(
            initialValue = null,
            producer = {
                withContext(Dispatchers.IO) {
                    runCatching {
                        value = sardine.list(path, 1, true).map {
                            async {
                                runCatching {
                                    sardine.getResources("$path${it.name}/")[0]
                                }.getOrNull()
                            }
                        }.awaitAll().filterNotNull()
                    }
                }
            })
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues
        ) {
            item {
                ItemSubTitle {
                    Text(text = path)
                }
            }
            roundColumn {
                folderProduceState?.let { list ->
                    items(list) { davResource ->
                        when (davResource.isDirectory) {
                            true -> {
                                val folderPath = "$path${davResource.name}/"
                                ItemArrowRight(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Rounded.Folder,
                                            contentDescription = null
                                        )
                                    },
                                    text = { Text(text = davResource.name) },
                                    subText = {},
                                    actions = {
                                        AppSwitch(
                                            checked = webDavSource.folders.any { it == folderPath },
                                            onCheckedChange = {
                                                if (it) {
                                                    webDavSource.addFolder(folderPath)
                                                } else {
                                                    webDavSource.removeFolder(folderPath)
                                                }
                                            }
                                        )
                                    }) {
                                    mainNavController.navigate(
                                        ScreenDestination.WebDavFolderManager(
                                            indexInWebDavSourceList,
                                            folderPath
                                        )
                                    )
                                }
                            }

                            false -> {
                                Item(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Rounded.InsertDriveFile,
                                            contentDescription = null
                                        )
                                    },
                                    text = { Text(text = davResource.name) },
                                    subText = {}) {}
                            }
                        }
                    }
                }
            }
        }
    }
}