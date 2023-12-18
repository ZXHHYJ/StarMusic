package com.zxhhyj.music.ui.screen.webdav.config

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.config.musicFilesDir
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.logic.utils.FileUtils
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.view.AppButton
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemSlider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSubTitle
import com.zxhhyj.ui.view.item.ItemSwitcher
import com.zxhhyj.ui.view.roundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun WebDavConfigScreen(
    paddingValues: PaddingValues,
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.webdav)) })
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.CloudUpload,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.webdav)) },
                        subText = {
                            Text(
                                text = stringResource(
                                    id = R.string.media_lib_has_songs_size,
                                    WebDavMediaLibRepository.songs.size
                                )
                            )
                        },
                        checked = SettingRepository.EnableWebDav,
                        onCheckedChange = {
                            SettingRepository.EnableWebDav = it
                            PlayerManager.clearPlayList()
                            if (!it) {
                                WebDavMediaLibRepository.clear()
                            }
                        }
                    )
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Create,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.create_webdav_source)) },
                        subText = { },
                    ) {
                        mainNavController.navigate(
                            ScreenDestination.WebDavSourceModify(
                                WebDavMediaLibRepository.createWebSource()
                            )
                        )
                    }
                }
            }
            item {
                ItemSubTitle {
                    Text(text = stringResource(id = R.string.webdav_source))
                }
            }
            roundColumn {
                items(WebDavMediaLibRepository.WebDavSources) {
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Storage,
                                contentDescription = null
                            )
                        },
                        text = {
                            Text(
                                text = it.remark.takeIf { it.isNotEmpty() }
                                    ?: stringResource(id = R.string.nothing)
                            )
                        },
                        subText = {
                            Text(text = it.username.takeIf { it.isNotEmpty() }
                                ?: stringResource(id = R.string.nothing))
                        }) {
                        mainNavController.navigate(
                            ScreenDestination.WebDavSourceModify(
                                WebDavMediaLibRepository.WebDavSources.indexOf(it)
                            )
                        )
                    }
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Sync,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.sync_webdav)) },
                        subText = { },
                        enabled = SettingRepository.EnableWebDav && WebDavMediaLibRepository.WebDavSources.isNotEmpty() && WebDavMediaLibRepository.WebDavSources.any { it.folders.isNotEmpty() }
                    ) {
                        dialogNavController.navigate(DialogDestination.SyncWebDavMediaLib)
                    }
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    val cacheSize = SettingRepository.AndroidVideoCacheSize
                    val cacheSizeText = if (cacheSize >= 1024f) {
                        "${(cacheSize / 1024)} GB"
                    } else {
                        "$cacheSize MB"
                    }
                    ItemSlider(
                        text = { Text(text = stringResource(id = R.string.max_cache_size)) },
                        subText = {
                            Text(text = cacheSizeText)
                        },
                        value = cacheSize.toFloat(),
                        valueRange = 100f..10 * 1024f,
                        onValueChange = { newValue ->
                            val newCacheSize = if (newValue > 1024f) {
                                (newValue / 1024).toInt() * 1024
                            } else {
                                (newValue / 100f).toInt() * 100
                            }
                            SettingRepository.AndroidVideoCacheSize = newCacheSize
                        }
                    )
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    val cacheSize by produceState<Long?>(initialValue = null, producer = {
                        value = withContext(Dispatchers.IO) {
                            musicFilesDir?.let { FileUtils.getFolderSize(it) / (1024 * 1024) }
                        }
                    })
                    val activity = LocalContext.current as Activity
                    cacheSize?.let {
                        Item(
                            text = { Text(text = stringResource(id = R.string.webdav_cache_song)) },
                            subText = { Text(text = "$it MB") },
                            actions = {
                                AppButton(onClick = {
                                    musicFilesDir?.takeIf { FileUtils.deleteFolder(it) }?.let {
                                        PlayerManager.clearPlayList()
                                        activity.recreate()
                                    }

                                }, text = {
                                    Text(
                                        text = stringResource(
                                            id = R.string.clear
                                        )
                                    )
                                })
                            }) {
                        }
                    }
                }
            }
        }
    }
}

