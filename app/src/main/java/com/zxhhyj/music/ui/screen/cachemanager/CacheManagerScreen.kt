package com.zxhhyj.music.ui.screen.cachemanager

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.config.musicFilesDir
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.FileUtils
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.ui.view.AppButton
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemSlider
import com.zxhhyj.ui.view.item.ItemSpacer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CacheManagerScreen(
    paddingValues: PaddingValues
) {
    AppScaffold(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding(),
        topBar = { AppCenterTopBar(title = { Text(text = stringResource(id = R.string.cache_manager)) }) }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    val cacheSize = SettingRepository.AndroidVideoCacheSize
                    val cacheSizeText = if (cacheSize >= 1024f) {
                        "${(cacheSize / 1024)} GB"
                    } else {
                        "$cacheSize MB"
                    }
                    ItemSlider(
                        text = { Text(text = stringResource(id = R.string.webdav_max_cache_size)) },
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
                        Item(text = { Text(text = stringResource(id = R.string.webdav_cache_song)) },
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
                            }) {}
                    }
                }
            }
        }
    }
}